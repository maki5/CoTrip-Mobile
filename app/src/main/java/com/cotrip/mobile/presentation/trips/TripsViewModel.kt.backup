package com.cotrip.mobile.presentation.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cotrip.mobile.data.model.CreateTripRequest
import com.cotrip.mobile.data.model.Trip
import com.cotrip.mobile.data.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TripsUiState(
    val isLoading: Boolean = false,
    val trips: List<Trip> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false
)

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TripsUiState())
    val uiState: StateFlow<TripsUiState> = _uiState.asStateFlow()
    
    init {
        loadTrips()
    }
    
    fun loadTrips(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            
            tripRepository.getTrips(
                search = _uiState.value.searchQuery.takeIf { it.isNotBlank() }
            )
                .onSuccess { trips ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        trips = trips
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = exception.message
                    )
                }
        }
    }
    
    fun searchTrips(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadTrips()
    }
    
    fun createTrip(
        title: String,
        description: String,
        destination: String,
        startDate: String,
        endDate: String,
        budget: Double?,
        isPublic: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val request = CreateTripRequest(
                title = title,
                description = description,
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                budget = budget,
                isPublic = isPublic
            )
            
            tripRepository.createTrip(request)
                .onSuccess { trip ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        trips = listOf(trip) + _uiState.value.trips
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }
    
    fun deleteTrip(tripId: String) {
        viewModelScope.launch {
            tripRepository.deleteTrip(tripId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        trips = _uiState.value.trips.filter { it.id != tripId }
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(error = exception.message)
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
