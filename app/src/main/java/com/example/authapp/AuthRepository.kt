package com.example.authapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Wraps Firebase Authentication (email/password) for the app.
 *
 * Requires a valid google-services.json (downloaded from your Firebase
 * console for project "gravuregrid") placed in the app/ module folder,
 * and Email/Password sign-in enabled under
 * Firebase Console -> Authentication -> Sign-in method.
 */
class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    data class AuthResult(val success: Boolean, val message: String)

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun register(name: String, email: String, password: String): AuthResult {
        val normalizedEmail = email.trim()

        if (name.isBlank()) return AuthResult(false, "Name cannot be empty")
        if (normalizedEmail.isBlank()) return AuthResult(false, "Enter a valid email address")
        if (password.length < 6) return AuthResult(false, "Password must be at least 6 characters")

        return try {
            val result = auth.createUserWithEmailAndPassword(normalizedEmail, password).await()

            // Save display name on the Firebase user profile
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            result.user?.updateProfile(profileUpdates)?.await()

            AuthResult(true, "Account created successfully")
        } catch (e: Exception) {
            AuthResult(false, e.localizedMessage ?: "Registration failed")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        val normalizedEmail = email.trim()
        return try {
            auth.signInWithEmailAndPassword(normalizedEmail, password).await()
            AuthResult(true, "Login successful")
        } catch (e: Exception) {
            AuthResult(false, e.localizedMessage ?: "Login failed")
        }
    }

    fun logout() {
        auth.signOut()
    }
}
