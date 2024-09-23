package com.polytech.app.data.repository

import com.polytech.app.data.model.Post
import com.polytech.app.data.model.AccommodationResponse
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("osm-france-tourism-accommodation/records")
    suspend fun getAccommodations(): AccommodationResponse
}