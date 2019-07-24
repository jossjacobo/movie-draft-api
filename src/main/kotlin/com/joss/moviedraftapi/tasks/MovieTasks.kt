package com.joss.moviedraftapi.tasks

import com.joss.moviedraftapi.movie.MovieService
import com.joss.moviedraftapi.network.boxofficemojo.BoxOfficeMojoClient
import com.joss.moviedraftapi.network.themoviedb.TmdbClient
import com.joss.moviedraftapi.tmdbconfig.TmdbConfigService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Mark this class an injectable component so that the Spring environment will create
 * an instance of this class when it starts up.
 */
@Component
class MovieTasks(
        private val movieService: MovieService,
        private val tmdbConfigService: TmdbConfigService) {

    private val log = LoggerFactory.getLogger(MovieTasks::class.java)

    /**
     * Runs at midnight
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun updateTmdbConfig() {
        log.info("Updating TMDB Config")
        var config = TmdbClient.config()
        if (config != null) {
            config = tmdbConfigService.set(config)
            log.info("Set Config: $config")
        }
    }

    /**
     * Runs at 1am
     */
    @Scheduled(cron = "0 0 1 * * *")
    fun getUpcomingMovies() {
        log.info("Fetching Upcoming Movies")

        // Grab upcoming movies
        var movies = TmdbClient.upcoming().list

        // Get and Add Details
        movies = movies?.map { TmdbClient.movie(it.id) ?: it }

        // Build image urls and update in DB
        tmdbConfigService.get().ifPresent { config ->
            movies?.forEach { movie ->

                // Backdrop images and path
                if (!movie.backdropPath.isNullOrBlank()) {
                    val backdrops = HashMap<String, String>()
                    config.images?.backdropSize?.forEach { size -> backdrops[size] = "${config.images?.secureBaseUrl}$size${movie.backdropPath}" }
                            .apply { movie.backdropImages = backdrops }
                }

                // Poster images and path
                if (!movie.posterPath.isNullOrBlank()) {
                    val posters = HashMap<String, String>()
                    config.images?.backdropSize?.forEach { size -> posters[size] = "${config.images?.secureBaseUrl}$size${movie.posterPath}" }
                            .apply { movie.posterImages = posters }
                }

                // Update in DB
                movieService.update(movie).also { log.info("Saving Movie: [$it]") }
            }
        }
    }

    /**
     * Runs at 2am
     *
     * Create task for finding BoxOfficeMojoId if movie doesn't have one
     */
    @Scheduled(cron = "0 0 2 * * *")
    fun getBoxOfficeMojoID() {
        log.info("Finding Box Office Mojo ID for movies missing it")
        val movies = movieService.getAllMissingBoxOfficeMojoID()
        movies.forEach { movie ->
            movie.apply {
                this.boxOfficeMojoId = BoxOfficeMojoClient.findBoxOfficeMojoId(this)
                this.boxOfficeMojoId?.let {
                    log.info("Saving Movie: [$movie]")
                    movieService.update(movie)
                }
            }
        }
    }

    /**
     * Runs at 3am
     */
    @Scheduled(cron = "0 0 3 * * *")
    fun getBoxOfficeMojoRevenue() {
        log.info("Updating Box Office Mojo Revenues")
        val movie = movieService.getMoviesWithBoxOfficeMojoID()
        movie.forEach { m -> BoxOfficeMojoClient.withRevenue(m).let { movieService.update(it) } }
    }

}





















