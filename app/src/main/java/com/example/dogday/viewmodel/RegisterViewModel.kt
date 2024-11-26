package com.example.dogday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.UserSession
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun registerUser() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _registerError.value = "Email and password must not be empty"
            return
        }

        if (!isValidEmail(_email.value)) {
            _registerError.value = "Invalid email format"
            return
        }

        if (_password.value.contains("\\s".toRegex())) {
            _registerError.value = "Password cannot contain spaces or tabs"
            return
        }

        viewModelScope.launch {
            Firebase.auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = Firebase.auth.currentUser?.uid ?: ""
                        UserSession.uid = uid
                        UserSession.email = _email.value
                        _registerSuccess.value = true
                    } else {
                        _registerError.value = task.exception?.localizedMessage
                    }
                }
        }
    }

    fun clearRegisterError() {
        _registerError.value = null
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
        )
        return emailPattern.matcher(email).matches()
    }
}

