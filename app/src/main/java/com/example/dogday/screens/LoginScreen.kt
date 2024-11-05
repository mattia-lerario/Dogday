// LoginScreen.kt

package com.example.dogday.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogday.viewmodel.LogInViewModel
import com.example.dogday.R
import com.example.dogday.ui.theme.InputBackgroundLight
import com.example.dogday.ui.theme.ButtonColorLight
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavController, logInViewModel: LogInViewModel = viewModel()) {
    val email by logInViewModel.email.collectAsState()
    val password by logInViewModel.password.collectAsState()
    val loginError by logInViewModel.loginError.collectAsState()
    val loginSuccess by logInViewModel.loginSuccess.collectAsState()

    if (loginSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("home")
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            // Layout for landscape orientation
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo on the left side
                Image(
                    painter = painterResource(R.drawable.dogday_logo),
                    contentDescription = "DogDay Logo",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )

                // Inputs, buttons, and error message centered in the middle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = email,
                        onValueChange = { logInViewModel.onEmailChange(it) },
                        label = { Text("Email", color = Color.Black) },
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = InputBackgroundLight,
                            unfocusedContainerColor = InputBackgroundLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.small
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password,
                        onValueChange = { logInViewModel.onPasswordChange(it) },
                        label = { Text("Password", color = Color.Black) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = InputBackgroundLight,
                            unfocusedContainerColor = InputBackgroundLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.small
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { logInViewModel.loginUser() },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text(
                                "Login",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { navController.navigate("register") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text(
                                "Register",
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
                        LaunchedEffect(Unit) {
                            logInViewModel.clearLoginError()
                        }
                    }

                }

                    // Dog image on the right side
                    Image(
                        painter = painterResource(R.drawable.dog_cartoon),
                        contentDescription = "Dog Image",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }
        } else {
            // Layout for portrait orientation
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.dogday_logo),
                    contentDescription = "DogDay Logo",
                    modifier = Modifier.size(350.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(18.dp))

                TextField(
                    value = email,
                    onValueChange = { logInViewModel.onEmailChange(it) },
                    label = { Text("Email", color = Color.Black) },
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = InputBackgroundLight,
                        unfocusedContainerColor = InputBackgroundLight,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.small
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { logInViewModel.onPasswordChange(it) },
                    label = { Text("Password", color = Color.Black) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = InputBackgroundLight,
                        unfocusedContainerColor = InputBackgroundLight,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.small
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {logInViewModel.loginUser()},
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight)
                    ) {
                        Text("Login", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight)
                    ) {
                        Text("Register", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                loginError?.let { error ->
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(R.drawable.dog_cartoon),
                    contentDescription = "Dog Image",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

