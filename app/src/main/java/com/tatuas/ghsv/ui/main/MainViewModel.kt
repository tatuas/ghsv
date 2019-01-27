package com.tatuas.ghsv.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.tatuas.ghsv.AppExecutors
import com.tatuas.ghsv.data.api.GitHubApiService
import com.tatuas.ghsv.data.db.User
import com.tatuas.ghsv.data.db.UserDatabase
import com.tatuas.ghsv.data.repository.UserBoundaryCallback
import com.tatuas.ghsv.data.repository.UserPagingRequestHelper
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class MainViewModel(gitHubApiService: GitHubApiService, userDatabase: UserDatabase) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 60
    }

    private val disposables = CompositeDisposable()

    private val executor = AppExecutors.PAGING

    private val dataSourceFactory = userDatabase.dao().getUserList()

    private val config = PagedList.Config
            .Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()

    private val boundaryCallback = UserBoundaryCallback(executor, userDatabase, gitHubApiService)

    private val _initializedLiveData = MutableLiveData<Boolean>()

    val userListLiveData: LiveData<PagedList<User>> =
            LivePagedListBuilder(dataSourceFactory, config)
                    .setBoundaryCallback(boundaryCallback)
                    .setFetchExecutor(executor)
                    .build()

    val stateLiveData: LiveData<State> = Transformations.switchMap(
            boundaryCallback.helper.stateLiveData) {
        MutableLiveData<State>().also { liveData ->
            liveData.value = when (it) {
                UserPagingRequestHelper.State.Loading -> State.Loading
                UserPagingRequestHelper.State.Loaded -> State.Loaded
                else -> {
                    when ((it as? UserPagingRequestHelper.State.Error)?.throwable) {
                        is UnknownHostException -> State.Offline
                        else -> State.Error
                    }
                }
            }
        }
    }

    fun initializeUserList() {
        if (true == _initializedLiveData.value) return
        boundaryCallback.helper.startStateListening()
        refreshUserList()
    }

    fun refreshUserList() {
        _initializedLiveData.value = true
        boundaryCallback.loadInitial()
    }

    override fun onCleared() {
        boundaryCallback.helper.stopStateListening()
        disposables.clear()
        super.onCleared()
    }

    sealed class State {
        object Loaded : State()
        object Loading : State()
        object Error : State()
        object Offline : State()
    }
}
