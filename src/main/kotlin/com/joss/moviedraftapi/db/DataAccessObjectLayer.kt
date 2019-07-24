package com.joss.moviedraftapi.db

import com.joss.moviedraftapi.movie.MovieModel
import com.joss.moviedraftapi.tmdbconfig.TmdbConfigModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : MongoRepository<MovieModel, String>

@Repository
interface TmdbConfigRepository: MongoRepository<TmdbConfigModel, String>