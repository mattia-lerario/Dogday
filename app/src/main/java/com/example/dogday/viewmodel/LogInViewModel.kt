package com.example.dogday.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun loginUser(context: Context) {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _loginError.value = "Email and password must not be empty"
            return
        }

        viewModelScope.launch {
            Firebase.auth.signInWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginSuccess.value = true
                        logLoginEvent()

                        val sharedPreferences = context.getSharedPreferences("dogday_preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                    } else {
                        _loginError.value = task.exception?.localizedMessage
                    }
                }
        }
    }

    private fun logLoginEvent() {
        val analytics = Firebase.analytics
        analytics.logEvent("login") {
            param("method", "email")
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }

    fun resetLoginSuccess() {
        _loginSuccess.value = false
    }
    fun logoutUser(context: Context) {
        Firebase.auth.signOut()

        val sharedPreferences = context.getSharedPreferences("dogday_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

        _loginSuccess.value = false
        _email.value = ""
        _password.value = ""
    }
}
