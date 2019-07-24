package com.joss.moviedraftapi.movie

import com.fasterxml.jackson.annotation.JsonProperty

data class MoviesModel(
    @JsonProperty("results") val list: List<MovieModel>? = emptyList()
)