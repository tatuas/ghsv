package com.tatuas.ghsv.data.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.tatuas.ghsv.data.api.GitHubApi
import com.tatuas.ghsv.data.db.UserDatabase
import com.tatuas.ghsv.ui.detail.DetailViewModel
import com.tatuas.ghsv.ui.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val gitHubApiService = GitHubApi.getService(context)
    private val userDatabase = UserDatabase.getInstance(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(gitHubApiService, userDatabase) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(gitHubApiService) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}