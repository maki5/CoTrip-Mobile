package com.cotrip.mobile.data.repository

import com.cotrip.mobile.data.model.*
import com.cotrip.mobile.data.network.CoTripApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor(
    private val apiService: CoTripApiService,
    private val authRepository: AuthRepository
) {
    
    suspend fun getTrips(
        page: Int = 1,
        limit: Int = 20,
        search: String? = null,
        destination: String? = null
    ): Result<List<Trip>> {
        return try {
            val token = authRepository.getCurrentAccessToken()
            val authHeader = token?.let { "Bearer $it" }
            
            val response = apiService.getTrips(authHeader, page, limit, search, destination)
            
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch trips: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTripById(tripId: String): Result<Trip> {
        return try {
            val token = authRepository.getCurrentAccessToken()
            val authHeader = token?.let { "Bearer $it" }
            
            val response = apiService.getTripById(tripId, authHeader)
            
            if (response.isSuccessful) {
                response.body()?.let { trip ->
                    Result.success(trip)
                } ?: Result.failure(Exception("Trip not found"))
            } else {
                Result.failure(Exception("Failed to fetch trip: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createTrip(request: CreateTripRequest): Result<Trip> {
        return try {
            val token = authRepository.getCurrentAccessToken()
                ?: return Result.failure(Exception("Not authenticated"))
            
            val response = apiService.createTrip("Bearer $token", request)
            
            if (response.isSuccessful) {
                response.body()?.let { trip ->
                    Result.success(trip)
                } ?: Result.failure(Exception("Failed to create trip"))
            } else {
                Result.failure(Exception("Failed to create trip: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateTrip(tripId: String, request: UpdateTripRequest): Result<Trip> {
        return try {
            val token = authRepository.getCurrentAccessToken()
                ?: return Result.failure(Exception("Not authenticated"))
            
            val response = apiService.updateTrip(tripId, "Bearer $token", request)
            
            if (response.isSuccessful) {
                response.body()?.let { trip ->
                    Result.success(trip)
                } ?: Result.failure(Exception("Failed to update trip"))
            } else {
                Result.failure(Exception("Failed to update trip: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteTrip(tripId: String): Result<Unit> {
        return try {
            val token = authRepository.getCurrentAccessToken()
                ?: return Result.failure(Exception("Not authenticated"))
            
            val response = apiService.deleteTrip(tripId, "Bearer $token")
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete trip: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getActivities(tripId: String): Result<List<Activity>> {
        return try {
            val token = authRepository.getCurrentAccessToken()
            val authHeader = token?.let { "Bearer $it" }
            
            val response = apiService.getActivities(tripId, authHeader)
            
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch activities: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createActivity(tripId: String, request: CreateActivityRequest): Result<Activity> {
        return try {
            val token = authRepository.getCurrentAccessToken()
                ?: return Result.failure(Exception("Not authenticated"))
            
            val response = apiService.createActivity(tripId, "Bearer $token", request)
            
            if (response.isSuccessful) {
                response.body()?.let { activity ->
                    Result.success(activity)
                } ?: Result.failure(Exception("Failed to create activity"))
            } else {
                Result.failure(Exception("Failed to create activity: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateActivity(activityId: String, request: CreateActivityRequest): Result<Activity> {
        return try {
            val token = authRepository.getCurrentAccessToken()
                ?: return Result.failure(Exception("Not authenticated"))
            
            val response = apiService.updateActivity(activityId, "Bearer $token", request)
            
            if (response.isSuccessful) {
                response.body()?.let { activity ->
                    Result.success(activity)
                } ?: Result.failure(Exception("Failed to update activity"))
            } else {
                Result.failure(Exception("Failed to update activity: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteActivity(activityId: String): Result<Unit> {
        return try {
            val token = authRepository.getCurrentAccessToken()
                ?: return Result.failure(Exception("Not authenticated"))
            
            val response = apiService.deleteActivity(activityId, "Bearer $token")
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete activity: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
