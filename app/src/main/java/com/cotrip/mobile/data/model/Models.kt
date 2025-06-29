package com.cotrip.mobile.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class User(
    val id: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatarUrl: String? = null,
    val createdAt: String
)

@Serializable
data class Trip(
    val id: String,
    val title: String,
    val description: String? = null,
    val destination: String,
    val startDate: String, // Will be converted to LocalDate
    val endDate: String,   // Will be converted to LocalDate
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String,
    val isPublic: Boolean = false,
    val budget: Double? = null,
    val participants: List<TripParticipant> = emptyList()
)

@Serializable
data class TripParticipant(
    val id: String,
    val tripId: String,
    val userId: String,
    val role: String = "viewer", // owner, editor, viewer
    val joinedAt: String
)

@Serializable
data class Activity(
    val id: String,
    val tripId: String,
    val title: String,
    val description: String? = null,
    val location: String,
    val scheduledDate: String, // Will be converted to LocalDateTime
    val durationMinutes: Int? = null,
    val cost: Double? = null,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String
)

// Request models
@Serializable
data class CreateTripRequest(
    val title: String,
    val description: String? = null,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val isPublic: Boolean = false,
    val budget: Double? = null
)

@Serializable
data class UpdateTripRequest(
    val title: String? = null,
    val description: String? = null,
    val destination: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val isPublic: Boolean? = null,
    val budget: Double? = null
)

@Serializable
data class CreateActivityRequest(
    val title: String,
    val description: String? = null,
    val location: String,
    val scheduledDate: String,
    val durationMinutes: Int? = null,
    val cost: Double? = null
)
