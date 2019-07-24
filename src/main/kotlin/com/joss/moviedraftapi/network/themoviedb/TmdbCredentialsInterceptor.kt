package com.joss.moviedraftapi.network.themoviedb

import okhttp3.Interceptor
import okhttp3.Response

class TmdbCredentialsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url

        if (!url.queryParameterNames.contains("api_key"))
            request = chain.request()
                .newBuilder()
                .url(url
                    .newBuilder()
                    .addEncodedQueryParameter("api_key", "6cec847025947cb3b239b9923284d0f0")
                    .build())
                .build()
        return chain.proceed(request)
    }

}