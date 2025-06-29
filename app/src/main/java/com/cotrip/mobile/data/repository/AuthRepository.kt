package com.cotrip.mobile.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan_tennert.supabase.SupabaseClient
import io.github.jan_tennert.supabase.gotrue.gotrue
import io.github.jan_tennert.supabase.gotrue.providers.Google
import io.github.jan_tennert.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.cotrip.mobile.BuildConfig
import com.cotrip.mobile.data.model.User

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient
) {
    private val dataStore = context.dataStore
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }
    
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }
    
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY] != null
    }
    
    val currentUser: Flow<User?> = dataStore.data.map { preferences ->
        val userId = preferences[USER_ID_KEY]
        val email = preferences[USER_EMAIL_KEY]
        
        if (userId != null && email != null) {
            User(
                id = userId,
                email = email,
                firstName = null,
                lastName = null,
                avatarUrl = null,
                createdAt = ""
            )
        } else {
            null
        }
    }
    
    suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = supabaseClient.gotrue.loginWith(io.github.jan_tennert.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
            }
            
            // Save tokens
            saveTokens(result.accessToken, result.refreshToken ?: "")
            saveUserInfo(result.user?.id ?: "", email)
            
            val user = User(
                id = result.user?.id ?: "",
                email = email,
                firstName = result.user?.userMetadata?.get("first_name") as? String,
                lastName = result.user?.userMetadata?.get("last_name") as? String,
                avatarUrl = result.user?.userMetadata?.get("avatar_url") as? String,
                createdAt = result.user?.createdAt?.toString() ?: ""
            )
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUpWithEmail(email: String, password: String, firstName: String, lastName: String): Result<User> {
        return try {
            val result = supabaseClient.gotrue.signUpWith(io.github.jan_tennert.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
                data = mapOf(
                    "first_name" to firstName,
                    "last_name" to lastName
                )
            }
            
            val user = User(
                id = result.user?.id ?: "",
                email = email,
                firstName = firstName,
                lastName = lastName,
                avatarUrl = null,
                createdAt = result.user?.createdAt?.toString() ?: ""
            )
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signInWithGoogle(): Result<User> {
        return try {
            val account = signInWithGoogleInternal()
            val idToken = account.idToken ?: throw Exception("No ID token received")
            
            val result = supabaseClient.gotrue.loginWith(Google) {
                idToken = idToken
            }
            
            // Save tokens
            saveTokens(result.accessToken, result.refreshToken ?: "")
            saveUserInfo(result.user?.id ?: "", account.email ?: "")
            
            val user = User(
                id = result.user?.id ?: "",
                email = account.email ?: "",
                firstName = account.givenName,
                lastName = account.familyName,
                avatarUrl = account.photoUrl?.toString(),
                createdAt = result.user?.createdAt?.toString() ?: ""
            )
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun signInWithGoogleInternal() = suspendCoroutine { continuation ->
        googleSignInClient.signInIntent.let { signInIntent ->
            // This would typically be handled by an Activity result launcher
            // For now, we'll simulate the flow
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
                val account = task.getResult(ApiException::class.java)
                continuation.resume(account)
            } catch (e: ApiException) {
                throw Exception("Google sign-in failed: ${e.statusCode}")
            }
        }
    }
    
    suspend fun signOut() {
        try {
            supabaseClient.gotrue.logout()
            googleSignInClient.signOut()
        } catch (e: Exception) {
            // Handle error
        } finally {
            clearTokens()
        }
    }
    
    suspend fun getCurrentAccessToken(): String? {
        return dataStore.data.first()[ACCESS_TOKEN_KEY]
    }
    
    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }
    
    private suspend fun saveUserInfo(userId: String, email: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
        }
    }
    
    private suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
