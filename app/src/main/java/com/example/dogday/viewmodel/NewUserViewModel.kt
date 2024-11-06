package com.example.dogday.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dogday.FirestoreInteractions
import com.example.dogday.User
import com.example.dogday.UserSession
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NewUserViewModel : ViewModel() {

    private val firestoreInteractions = FirestoreInteractions()

    // User session variables
    private val email = UserSession.email ?: ""
    private val uid = UserSession.uid ?: ""

    // Mutable states for the screen
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var birthday = mutableStateOf(0L)  // Store selected date here
    var showDatePicker = mutableStateOf(false)
    var saveSuccess = mutableStateOf(false)

    // Function to save user data to Firestore
    fun saveUserData() {
        if (firstName.value.isNotEmpty() && phoneNumber.value.isNotEmpty()) {
            val user = User(
                uid = uid,
                email = email,
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value,
                birthday = birthday.value
            )

            firestoreInteractions.addUser(user)

            Firebase.auth.currentUser?.uid?.let { uid ->
                FirebaseFirestore.getInstance().collection("ddcollection").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            saveSuccess.value = true
                        }
                    }
            }
        }
    }
}
