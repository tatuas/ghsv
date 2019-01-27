package com.tatuas.ghsv.data.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.tatuas.ghsv.data.api.GitHubApiService
import com.tatuas.ghsv.data.db.User
import com.tatuas.ghsv.data.db.UserDatabase
import java.util.concurrent.Executor

class UserBoundaryCallback(executor: Executor,
                           userDatabase: UserDatabase,
                           gitHubApiService: GitHubApiService) : PagedList.BoundaryCallback<User>() {
    val helper = UserPagingRequestHelper(executor, userDatabase, gitHubApiService)

    @MainThread
    override fun onZeroItemsLoaded() {
    }

    @MainThread
    override fun onItemAtFrontLoaded(itemAtFront: User) {
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: User) {
        helper.loadAfterIfNotRunning(itemAtEnd.id)
    }

    fun loadInitial() {
        helper.loadInitialIfNotRunning()
    }
}
