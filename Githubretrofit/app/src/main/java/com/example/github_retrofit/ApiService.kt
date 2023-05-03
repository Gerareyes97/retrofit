package com.example.github_retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users/{username}/repos")
    fun getRepositorios(@Path("username") username: String): Call<List<Repository>>
}