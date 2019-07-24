package com.joss.moviedraftapi.tmdbconfig

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tmdb-config")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TmdbConfigModel(
        @Id
        var id: String?,
        val images: Images? = null,
        @JsonProperty("change_keys")
        val changeKeys: List<String>? = emptyList(),
        var lastUpdateAt: String? = null
)

data class Images(
        @JsonProperty("base_url")
        val baseUrl: String? = null,
        @JsonProperty("secure_base_url")
        val secureBaseUrl: String? = null,
        @JsonProperty("backdrop_sizes")
        val backdropSize: List<String>? = emptyList(),
        @JsonProperty("logo_sizes")
        val logoSizes: List<String>? = emptyList(),
        @JsonProperty("poster_sizes")
        val posterSizes: List<String>? = emptyList(),
        @JsonProperty("profile_sizes")
        val profileSizes: List<String>? = emptyList(),
        @JsonProperty("still_sizes")
        val stillSizes: List<String>? = emptyList()
)