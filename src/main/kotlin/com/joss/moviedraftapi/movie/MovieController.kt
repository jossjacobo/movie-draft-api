package com.joss.moviedraftapi.movie

import com.joss.moviedraftapi.network.themoviedb.TmdbClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MovieController(private val movieService: MovieService) {

    @GetMapping("/movie/{movie-id}")
    fun movie(@PathVariable("movie-id") movieId: String): MovieModel {
        val movie = TmdbClient.movie(movieId)
        return movie ?: MovieModel(id = "-1", title = "")
    }

    @GetMapping("/movies")
    fun movies(): List<MovieModel> = movieService.getAll()

}