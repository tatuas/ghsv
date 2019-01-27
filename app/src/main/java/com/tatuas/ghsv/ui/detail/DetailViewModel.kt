package com.tatuas.ghsv.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tatuas.ghsv.data.api.GitHubApiService
import com.tatuas.ghsv.data.api.GitHubDetailUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.UnknownHostException

class DetailViewModel(private val gitHubApiService: GitHubApiService) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _titleLiveData = MutableLiveData<String>()
    val titleLiveData: LiveData<String> = _titleLiveData

    private val _detailLiveData = MutableLiveData<GitHubDetailUser>()
    val detailLiveData: LiveData<GitHubDetailUser> = _detailLiveData

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private val _initializedLiveData = MutableLiveData<Boolean>()

    fun initialize(name: String?) {
        if (true == _initializedLiveData.value) return

        if (name.isNullOrEmpty()) {
            _stateLiveData.value = State.Error
            return
        }

        _stateLiveData.value = State.Loading

        gitHubApiService.getUserDetail(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            _titleLiveData.value = it.login
                            _detailLiveData.value = it

                            _stateLiveData.value = State.Loaded

                            _initializedLiveData.value = true
                        },
                        onError = {
                            Timber.d(it)

                            _stateLiveData.value = when (it) {
                                is UnknownHostException -> State.Offline
                                else -> State.Error
                            }

                            _initializedLiveData.value = false
                        })
                .addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    sealed class State {
        object Loaded : State()
        object Loading : State()
        object Offline : State()
        object Error : State()
    }
}
