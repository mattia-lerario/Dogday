// LoginScreen.kt

package com.example.dogday

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.max
import com.example.dogday.ui.theme.InputBackgroundLight
import com.example.dogday.ui.theme.ButtonColorLight
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.dogday_logo), // Add your logo drawable here
            contentDescription = "DogDay Logo",
            modifier = Modifier.size(350.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight
            ),
            shape = MaterialTheme.shapes.small


        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Black) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        Firebase.auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val uid = Firebase.auth.currentUser?.uid
                                    if (uid != null) {
                                        val firestore = FirebaseFirestore.getInstance()
                                        firestore.collection("ddcollection").document(uid).get()
                                            .addOnSuccessListener { document ->
                                                if (document.exists()) {
                                                    val dogsMap = document.get("dogs") as? Map<*, *>
                                                    if (dogsMap == null || dogsMap.isEmpty()) {
                                                        navController.navigate("addDogScreen")
                                                    } else {
                                                        navController.navigate("home")
                                                    }
                                                }
                                            }
                                    }

                                    val analytics = Firebase.analytics
                                    analytics.logEvent("login") {
                                        param("method", "email")
                                    }
                                } else {
                                    loginError = task.exception?.localizedMessage
                                }
                            }
                    } else {
                        loginError = "Email and password must not be empty"
                    }
                },

            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                contentPadding = PaddingValues(vertical = 8.dp)

        ) {
            Text("Login",
                fontWeight = FontWeight.Bold

                )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text("Register",
                fontWeight = FontWeight.Bold
                )
         }

        }

        Spacer(modifier = Modifier.height(8.dp))


        loginError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Image(
            painter = painterResource(R.drawable.dog_cartoon),
            contentDescription = "Dog Image",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )

    }
}
