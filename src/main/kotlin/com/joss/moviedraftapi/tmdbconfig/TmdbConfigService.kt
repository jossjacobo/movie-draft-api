package com.joss.moviedraftapi.tmdbconfig

import com.joss.moviedraftapi.db.TmdbConfigRepository
import com.joss.moviedraftapi.utils.DateUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*

@Service
class TmdbConfigService(private val tmdbConfigRepository: TmdbConfigRepository) {

    fun get(): Optional<TmdbConfigModel> {
        var config: Optional<TmdbConfigModel> = Optional.empty()
        try {
            val page: Page<TmdbConfigModel> = tmdbConfigRepository.findAll(Pageable.unpaged())
            config = Optional.of(page.first())
        } catch (e: Exception) {
            println()
        }
        return config
    }

    fun set(config: TmdbConfigModel): TmdbConfigModel {
        val lastUpdated = DateUtil.now()
        val existingConfig = get()
        if (existingConfig.isPresent) {
            tmdbConfigRepository.save(config.apply {
                this.id = existingConfig.get().id
                this.lastUpdateAt = lastUpdated
            })
        } else {
            tmdbConfigRepository.insert(config.apply {
                this.lastUpdateAt = lastUpdated
            })
        }
        return config
    }

    fun remove(): Optional<TmdbConfigModel> {
        return get().apply {
            this.ifPresent {
                tmdbConfigRepository.delete(it)
            }
        }
    }
}