package com.tatuas.ghsv.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tatuas.ghsv.data.api.GitHubApiService
import com.tatuas.ghsv.data.db.User
import com.tatuas.ghsv.data.db.UserDatabase
import com.tatuas.ghsv.util.PagingRequestHelper
import timber.log.Timber
import java.util.concurrent.Executor

class UserPagingRequestHelper(private val executor: Executor,
                              private val userDatabase: UserDatabase,
                              private val gitHubApiService: GitHubApiService
) : PagingRequestHelper(executor) {
    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private val listener = PagingRequestHelper.Listener { report ->
        when {
            report.hasRunning() -> _stateLiveData.postValue(State.Loading)
            report.hasError() -> {
                val exception = PagingRequestHelper.RequestType
                        .values()
                        .mapNotNull { report.getErrorFor(it) }
                        .first()

                _stateLiveData.postValue(State.Error(exception))
            }
            else -> _stateLiveData.postValue(State.Loaded)
        }
    }

    @MainThread
    fun loadInitialIfNotRunning() {
        runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            executor.execute {
                fetch(null, RequestType.INITIAL, callback)
            }
        }
    }

    @MainThread
    fun loadAfterIfNotRunning(id: Long) {
        runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            executor.execute { fetch(id, RequestType.AFTER, callback) }
        }
    }

    fun startStateListening() {
        addListener(listener)
    }

    fun stopStateListening() {
        removeListener(listener)
    }

    @WorkerThread
    private fun fetch(id: Long?, requestType: RequestType, callback: Request.Callback) =
            gitHubApiService.getUserList(id)
                    .map { githubListUserList -> githubListUserList.map { item -> User.from(item) } }
                    .map {
                        if (RequestType.INITIAL == requestType) {
                            userDatabase.dao().clear()
                        }

                        userDatabase.dao().save(it)
                    }
                    .map { callback.recordSuccess() }
                    .onErrorReturn {
                        Timber.d(it)
                        callback.recordFailure(it)
                    }
                    .blockingSingle()

    sealed class State {
        object Loading : State()
        object Loaded : State()
        data class Error(val throwable: Throwable?) : State()
    }
}
