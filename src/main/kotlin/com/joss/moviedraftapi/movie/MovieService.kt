package com.joss.moviedraftapi.movie

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.joss.moviedraftapi.db.BasicCrud
import com.joss.moviedraftapi.utils.DateUtil
import com.mongodb.client.result.DeleteResult
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Service


@Service
class MovieService(val mongoTemplate: MongoTemplate) : BasicCrud<String, MovieModel> {

    fun existById(movie: MovieModel): Boolean = mongoTemplate.exists(Query.query(Criteria.where("id").isEqualTo(movie.id)), MovieModel::class.java)

    override fun getAll(): List<MovieModel> = mongoTemplate.findAll(MovieModel::class.java)

    override fun getAll(page: Int, count: Int): List<MovieModel> {
        val query = Query()
        query.skip((page * count).toLong())
        query.limit(count)
        return mongoTemplate.find(query, MovieModel::class.java)
    }

    override fun getById(id: String): MovieModel? = mongoTemplate.findById(id)

    override fun insert(movie: MovieModel): MovieModel = movie.apply {
        this.lastUpdateAt = DateUtil.now()
        mongoTemplate.save(this)
    }


    /**
     * Method for updating a movie data with only fields provided or inserting a new movie
     */
    override fun update(movie: MovieModel): MovieModel? {
        // Set lastUpdatedAt
        movie.lastUpdateAt = DateUtil.now()

        // If movie is already in the DB perform an update, else insert
        if(existById(movie)) {
            // Create query matching on ID
            val query = Query.query(Criteria.where("id").isEqualTo(movie.id))

            // Copy all values and field to a Map
            val objectMapper = ObjectMapper()
            //exclude null
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
            val objectAsMap: HashMap<String, *> = objectMapper.convertValue(movie, Map::class.java) as HashMap<String, *>
            //exclude id
            objectAsMap.remove("id")

            //Create Update Criteria
            val update = Update()
            objectAsMap.onEach { update.set(it.key, it.value) }

            mongoTemplate.updateFirst(query, update, MovieModel::class.java)
            return getById(movie.id)
        } else {
            return insert(movie)
        }

    }

    override fun deleteById(movie: MovieModel): DeleteResult = mongoTemplate.remove(movie)

    fun getAllMissingBoxOfficeMojoID(): List<MovieModel> {
        val query = Query().addCriteria(
                Criteria.where("boxOfficeMojoId").exists(false)
                        .orOperator(Criteria.where("boxOfficeMojoId").`is`(null)))
        return mongoTemplate.find(query, MovieModel::class.java)
    }

    fun getMoviesWithBoxOfficeMojoID(): List<MovieModel> {
        val query = Query().addCriteria(
                Criteria.where("boxOfficeMojoId").exists(true))
        return mongoTemplate.find(query, MovieModel::class.java)
    }
}