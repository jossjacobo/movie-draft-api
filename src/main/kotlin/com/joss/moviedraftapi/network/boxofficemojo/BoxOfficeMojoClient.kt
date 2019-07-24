package com.joss.moviedraftapi.network.boxofficemojo

import com.joss.moviedraftapi.movie.MovieModel
import com.joss.moviedraftapi.movie.Revenue
import com.joss.moviedraftapi.utils.DateUtil
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object BoxOfficeMojoClient {

    private val log = LoggerFactory.getLogger(BoxOfficeMojoClient::class.java)
    private const val BOX_OFFICE_MOJO_HOST = "http://www.boxofficemojo.com"

    fun findBoxOfficeMojoId(movie: MovieModel): String? {
        log.info("Finding Box Office Mojo ID for - ${movie.id} - ${movie.title}")
        try {
            val releaseYear = LocalDate.parse(movie.releaseDate, DateTimeFormatter.ISO_DATE).year
            val searchResultsPage = Jsoup.connect("$BOX_OFFICE_MOJO_HOST/search/?q=${movie.title}").get()

            val dates: Elements = searchResultsPage.select("[href^=\"/schedule/?view\"]")
            val index = dates.indexOf(dates.find { LocalDate.parse(it.text(), DateTimeFormatter.ofPattern("M/d/yyyy")).year == releaseYear})
            if (index != -1) {
                val hrefList = searchResultsPage.select("td a[href^=\"/movies/?id=\"]")
                return hrefList[index].attr("href").replace("/movies/?id=", "")
            }
        }catch (e: Exception) {
            log.error(e.toString())
        }
        return null
    }

    fun withRevenue(movie: MovieModel): MovieModel{
        log.info("Finding Revenue for - ${movie.id} - ${movie.title} - ${movie.boxOfficeMojoId}")
        try {
            // Grab Movie Detail page
            val detailPage = Jsoup.connect("$BOX_OFFICE_MOJO_HOST/movies/?id=${movie.boxOfficeMojoId}").get()

            // Parse BoxOffice Results
            val results: Elements = detailPage.select(".mp_box_tab + .mp_box_content")

            // Find revenues values container
            val revenueContainer = results.find { it.text().contains("Domestic") }
            val elements = revenueContainer?.select("td b")

            // Grab domestic and world wide revenues
            val domestic = elements!![1].text()?.replace("""\D""".toRegex(), "")?.toInt() ?: 0
            val worldwide = elements.last().text()?.replace("""\D""".toRegex(), "")?.toInt() ?: 0

            // Add to revenues list and replace
            val revenueList = movie.revenues!!.toMutableList()
            revenueList.add(Revenue(domestic, worldwide, DateUtil.now()))

            movie.revenues = revenueList
        } catch (e: Exception) {
            log.error(e.toString())
        }
        return movie
    }
}