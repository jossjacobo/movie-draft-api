package com.joss.moviedraftapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MovieDraftApiApplication
fun main(args: Array<String>) {
    runApplication<MovieDraftApiApplication>(*args)
}