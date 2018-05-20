package com.tatuas.ghsv.data.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users")
    fun getUserList(@Query("since") since: Long?): Observable<List<GitHubListUser>>

    @GET("users/{userName}")
    fun getUserDetail(@Path("userName") userName: String): Observable<GitHubDetailUser>
}
