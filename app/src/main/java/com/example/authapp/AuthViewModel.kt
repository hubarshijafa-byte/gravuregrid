package com.example.authapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun isLoggedIn(): Boolean = repository.currentUser != null

    fun currentUser(): FirebaseUser? = repository.currentUser

    fun register(name: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.register(name, email, password)
            _isLoading.value = false
            onResult(result.success, result.message)
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.login(email, password)
            _isLoading.value = false
            onResult(result.success, result.message)
        }
    }

    fun logout() {
        repository.logout()
    }
}
