package com.cotrip.mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cotrip.mobile.presentation.auth.AuthViewModel
import com.cotrip.mobile.presentation.auth.LoginScreen
import com.cotrip.mobile.presentation.auth.SignUpScreen
import com.cotrip.mobile.presentation.trips.TripsScreen
import com.cotrip.mobile.presentation.trips.TripDetailScreen
import com.cotrip.mobile.presentation.trips.CreateTripScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Trips : Screen("trips")
    object TripDetail : Screen("trip_detail/{tripId}") {
        fun createRoute(tripId: String) = "trip_detail/$tripId"
    }
    object CreateTrip : Screen("create_trip")
}

@Composable
fun CoTripNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    
    val startDestination = if (authState.isLoggedIn) {
        Screen.Trips.route
    } else {
        Screen.Login.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Trips.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Screen.Trips.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Main app screens
        composable(Screen.Trips.route) {
            TripsScreen(
                onTripClick = { tripId ->
                    navController.navigate(Screen.TripDetail.createRoute(tripId))
                },
                onCreateTripClick = {
                    navController.navigate(Screen.CreateTrip.route)
                },
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Trips.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.TripDetail.route) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
            TripDetailScreen(
                tripId = tripId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.CreateTrip.route) {
            CreateTripScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTripCreated = { tripId ->
                    navController.navigate(Screen.TripDetail.createRoute(tripId)) {
                        popUpTo(Screen.CreateTrip.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
