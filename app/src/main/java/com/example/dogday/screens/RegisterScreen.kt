// RegisterScreen.kt

package com.example.dogday.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogday.R
import com.example.dogday.UserSession
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.ui.theme.InputBackgroundLight
import com.example.dogday.viewmodel.RegisterViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val registerError by registerViewModel.registerError.collectAsState()
    val registerSuccess by registerViewModel.registerSuccess.collectAsState()

    if (registerSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("newUser")
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ){
        val landscapeMode = maxWidth > maxHeight

        if (landscapeMode) {

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
                    Text(
                        text = "Let's get you registered!",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(bottom = 24.dp)
                    )

                    TextField(
                        value = email,
                        onValueChange = { registerViewModel.onEmailChange(it) },
                        label = { Text("Email", color = Color.Black) },
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = InputBackgroundLight,
                            unfocusedContainerColor = InputBackgroundLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = MaterialTheme.shapes.small
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password,
                        onValueChange = { registerViewModel.onPasswordChange(it) },
                        label = { Text("Password", color = Color.Black) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = InputBackgroundLight,
                            unfocusedContainerColor = InputBackgroundLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = MaterialTheme.shapes.small
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Already have an account? Click on Login.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { navController.navigate("login") },
                            Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("Login")
                        }

                        Button(
                            onClick = {registerViewModel.registerUser()},
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("Register")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    registerError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        LaunchedEffect(Unit) {
                            registerViewModel.clearRegisterError()}
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

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Let's get you registered!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(bottom = 24.dp)
                )

                TextField(
                    value = email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    label = { Text("Email", color = Color.Black) },
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputBackgroundLight,
                        unfocusedContainerColor = InputBackgroundLight,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = MaterialTheme.shapes.small
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    label = { Text("Password", color = Color.Black) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputBackgroundLight,
                        unfocusedContainerColor = InputBackgroundLight,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = MaterialTheme.shapes.small
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Already have an account? Click on Login.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.navigate("login") },
                        Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text("Login")
                    }

                    Button(
                        onClick = {registerViewModel.registerUser()},
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text("Register")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                registerError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    LaunchedEffect(Unit) {
                        registerViewModel.clearRegisterError()
                    }
                }

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
