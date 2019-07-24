package com.joss.moviedraftapi.db

import com.joss.moviedraftapi.movie.MovieModel
import com.mongodb.client.result.DeleteResult

interface BasicCrud<K, T> {
    fun getAll(): List<T>
    fun getAll(page: Int = 0, count: Int = 10): List<T>
    fun getById(id: K): T?
    fun insert(obj: T): T
    fun update(obj: T): MovieModel?
    fun deleteById(obj: T): DeleteResult
}