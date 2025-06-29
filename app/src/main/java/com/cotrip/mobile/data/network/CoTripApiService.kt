package com.cotrip.mobile.data.network

import com.cotrip.mobile.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface CoTripApiService {
    
    // Auth endpoints
    @GET("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>
    
    // Trip endpoints
    @GET("trips")
    suspend fun getTrips(
        @Header("Authorization") token: String?,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("search") search: String? = null,
        @Query("destination") destination: String? = null
    ): Response<List<Trip>>
    
    @GET("trips/{id}")
    suspend fun getTripById(
        @Path("id") tripId: String,
        @Header("Authorization") token: String?
    ): Response<Trip>
    
    @POST("trips")
    suspend fun createTrip(
        @Header("Authorization") token: String,
        @Body trip: CreateTripRequest
    ): Response<Trip>
    
    @PUT("trips/{id}")
    suspend fun updateTrip(
        @Path("id") tripId: String,
        @Header("Authorization") token: String,
        @Body trip: UpdateTripRequest
    ): Response<Trip>
    
    @DELETE("trips/{id}")
    suspend fun deleteTrip(
        @Path("id") tripId: String,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    // Activity endpoints
    @GET("trips/{tripId}/activities")
    suspend fun getActivities(
        @Path("tripId") tripId: String,
        @Header("Authorization") token: String?
    ): Response<List<Activity>>
    
    @POST("trips/{tripId}/activities")
    suspend fun createActivity(
        @Path("tripId") tripId: String,
        @Header("Authorization") token: String,
        @Body activity: CreateActivityRequest
    ): Response<Activity>
    
    @PUT("activities/{id}")
    suspend fun updateActivity(
        @Path("id") activityId: String,
        @Header("Authorization") token: String,
        @Body activity: CreateActivityRequest
    ): Response<Activity>
    
    @DELETE("activities/{id}")
    suspend fun deleteActivity(
        @Path("id") activityId: String,
        @Header("Authorization") token: String
    ): Response<Unit>
    
    // Participant endpoints
    @POST("trips/{tripId}/participants")
    suspend fun addParticipant(
        @Path("tripId") tripId: String,
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Response<TripParticipant>
    
    @DELETE("trips/{tripId}/participants/{userId}")
    suspend fun removeParticipant(
        @Path("tripId") tripId: String,
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<Unit>
}
