package com.example.dogday

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun NewUserScreen(navController: NavController) {
    val email = UserSession.email ?: ""
    val uid = UserSession.uid ?: ""

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val firestoreInteractions = FirestoreInteractions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome, Let's make a new account for you!")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val user = User(uid, email, name, phoneNumber)
                firestoreInteractions.addUser(user)
                val uid = Firebase.auth.currentUser?.uid
                if (uid != null) {
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("ddcollection").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val user = document.toObject(User::class.java)
                                if (user != null) {
                                    // Check if the dogs map is empty
                                    if (user.dogs.isEmpty()) {
                                        navController.navigate("addDogScreen")
                                    } else {
                                        navController.navigate("home")
                                    }
                                }
                            }
                        }
                }
            }
        }) {
            Text("Save information")
        }
    }
}
