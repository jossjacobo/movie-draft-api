package com.joss.moviedraftapi.movie

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.mongodb.lang.Nullable
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "movies")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MovieModel(
        @Id
        val id: String,
        @Nullable
        var boxOfficeMojoId: String? = null,
        val title: String? = null,
        val adult: Boolean? = false,
        @JsonProperty("backdrop_path")
        val backdropPath: String? = null,
        var backdropImages: Map<String, String> = emptyMap(),
        @JsonProperty("belongs_to_collection")
        val belongsToCollection: MovieCollection? = null,
        val budget: Number? = 0,
        val genres: List<MovieGenre>? = emptyList(),
        val homepage: String? = null,
        @JsonProperty("imdb_id")
        val imdbId: String? = null,
        @JsonProperty("original_language")
        val originalLanguage: String? = null,
        @JsonProperty("original_title")
        val originalTitle: String? = null,
        val overview: String? = null,
        val popularity: Number? = null,
        @JsonProperty("poster_path")
        val posterPath: String? = null,
        var posterImages: Map<String, String>? = emptyMap(),
        @JsonProperty("production_companies")
        val productionCompanies: List<ProductionCompany>? = emptyList(),
        @JsonProperty("production_countries")
        val productionCountries: List<ProductionCountries>? = emptyList(),
        @JsonProperty("release_date")
        val releaseDate: String? = null,
        var revenues: List<Revenue>? = emptyList(),
        val runtime: Number? = 0,
        @JsonProperty("spoken_languages")
        val spokenLanguages: List<SpokenLanguage>? = emptyList(),
        val status: String? = null,
        val tagline: String? = null,
        val video: Boolean? = false,
        @JsonProperty("vote_average")
        val voteAverage: Number? = 0,
        @JsonProperty("vote_count")
        val voteCount: Number? = null,
        var createdAt: String? = null,
        var lastUpdateAt: String? = null)

@Document(collection = "revenues")
data class Revenue(
        var domesticRevenue: Number? = 0,
        var worldWideRevenue: Number? = 0,
        var lastUpdateAt: String? = null
)

@Document(collection = "collections")
data class MovieCollection(
        val id: Number? = 0,
        val name: String? = null,
        @JsonProperty("poster_path") val posterPath: String? = null,
        @JsonProperty("backdrop_path") val backdropPath: String? = null
)

@Document(collection = "genre")
data class MovieGenre(
        val id: Number? = -1,
        val name: String? = null
)

@Document(collection = "production-company")
data class ProductionCompany(
        val id: Number? = -1,
        @JsonProperty("logo_path") val logoPath: String? = null,
        val name: String? = null,
        @JsonProperty("origin_country") val originCountry: String? = null
)

@Document(collection = "production-company")
data class ProductionCountries(
        @JsonProperty("iso_3166_1") val iso31661: String? = null,
        val name: String? = null
)

@Document(collection = "spoken-language")
data class SpokenLanguage(
        @JsonProperty("iso_639_1") val iso6391: String? = null,
        val name: String? = null
)