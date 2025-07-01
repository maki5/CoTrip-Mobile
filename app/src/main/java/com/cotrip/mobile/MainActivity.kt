package com.cotrip.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            CoTripTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CoTripApp()
                }
            }
        }
    }
}

@Composable
fun CoTripApp() {
    var currentScreen by remember { mutableStateOf("welcome") }
    
    when (currentScreen) {
        "welcome" -> WelcomeScreen(
            onNavigateToLogin = { currentScreen = "login" },
            onNavigateToTrips = { currentScreen = "trips" }
        )
        "login" -> LoginScreen(
            onNavigateBack = { currentScreen = "welcome" },
            onLoginSuccess = { currentScreen = "trips" }
        )
        "trips" -> TripsScreen(
            onNavigateBack = { currentScreen = "welcome" }
        )
    }
}

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToTrips: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to CoTrip!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Native Android app built with Kotlin & Jetpack Compose",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )
        
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Login")
        }
        
        Button(
            onClick = onNavigateToTrips,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Trips (Demo)")
        }
    }
}

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )
        
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Sign In")
        }
        
        TextButton(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}

@Composable
fun TripsScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Trips",
                style = MaterialTheme.typography.headlineMedium
            )
            TextButton(onClick = onNavigateBack) {
                Text("Back")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Mock trip cards
        repeat(3) { index ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Trip ${index + 1}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "This is a sample trip destination",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CoTripTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
