package com.example.dogday.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dogday.FirestoreInteractions
import com.example.dogday.R
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        // Firebase services
        val analytics = Firebase.analytics
        val auth = Firebase.auth

        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavigationHost(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier) {
    val firestoreInteractions = FirestoreInteractions()

    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable(DogScreen.Login.name) { LoginScreen(navController) }   // Use LoginScreen here
        composable(DogScreen.Home.name) { HomeScreen(navController) }      // Home Screen of the application
        composable(DogScreen.Map.name) { MapScreen(navController) }        // Map Screen
        composable(DogScreen.Register.name) { RegisterScreen(navController) } // Use RegisterScreen here
        composable(DogScreen.NewUser.name) { NewUserScreen(navController) } // No need to pass uid and email
        composable(DogScreen.AddDog.name) { AddDogScreen(navController) }


    }
}

enum class DogScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    Register(title = R.string.register),
    Home(title = R.string.home),
    Map(title = R.string.map),
    NewUser(title = R.string.newUser),
    AddDog(title = R.string.addDog),

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogAppBar(
    currentScreen: DogScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },

        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Tilbake"
                    )
                }
            }
        }
    )
}



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentDestination = navController.currentDestination?.route
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentDestination == "home",
            onClick = {
                if (currentDestination != "home") {
                    navController.navigate("home")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Map") },
            label = { Text("Map") },
            selected = currentDestination == "map",
            onClick = {
                if (currentDestination != "map") {
                    navController.navigate("map")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Login") },
            label = { Text("Login") },
            selected = currentDestination == "login",
            onClick = {
                if (currentDestination != "login") {
                    navController.navigate("login")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainApp()
}
