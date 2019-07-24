package com.joss.moviedraftapi.network.themoviedb

import okhttp3.OkHttpClient

object TmdbHttpClient {

    fun get(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TmdbCredentialsInterceptor())
        .build()


}