package com.joss.moviedraftapi.network.themoviedb

import com.fasterxml.jackson.module.kotlin.readValue
import com.joss.moviedraftapi.utils.JacksonMapper
import com.joss.moviedraftapi.movie.MovieModel
import com.joss.moviedraftapi.movie.MoviesModel
import com.joss.moviedraftapi.tmdbconfig.TmdbConfigModel
import okhttp3.Request
import okhttp3.Response

object TmdbClient {

    private const val API_HOST = "http://api.themoviedb.org/3"

    private val mapper = JacksonMapper.get()

    fun config(): TmdbConfigModel? {
        var config: TmdbConfigModel? = null
        try {
            val response = TmdbHttpClient.get()
                    .newCall(Request.Builder()
                            .url("$API_HOST/configuration")
                            .build())
                    .execute()
            config = mapper.readValue(response.body?.string(), TmdbConfigModel::class.java)
        } catch (e: Exception) {
            println(e.toString())
        }
        return config
    }

    fun movie(id: String): MovieModel? {
        var movie: MovieModel? = null
        try {
            val response: Response = TmdbHttpClient.get()
                    .newCall(Request.Builder()
                            .url("$API_HOST/movie/$id")
                            .build())
                    .execute()
            movie = mapper.readValue(response.body?.string(), MovieModel::class.java)
        } catch (e: Exception) {
            println(e.toString())
        }
        return movie
    }

    fun upcoming(): MoviesModel {
        var movies = MoviesModel()
        try {
            val response: Response = TmdbHttpClient.get()
                    .newCall(Request.Builder()
                            .url("$API_HOST/movie/upcoming")
                            .build())
                    .execute()

            val responseString: String? = response.body?.string()

            if (responseString != null)
                movies = mapper.readValue(responseString)
        } catch (e: Exception) {
            println(e.toString())
        }
        return movies
    }
}