package com.example.dogday.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentScreen = when {
        currentRoute?.startsWith("DogDetailScreen") == true -> DogScreen.DogDetail
        else -> DogScreen.valueOf(currentRoute ?: DogScreen.Login.name)
    }

    Scaffold(
        topBar = {
            DogAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()},
                currentScreen = currentScreen
            )
        },


        floatingActionButton = {
            if (currentScreen == DogScreen.DogDetail){
                FloatingActionButton(
                    onClick = { navController.navigate(route = DogScreen.SettingsScreen.name) },
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Legg til")
                }
            }},
        floatingActionButtonPosition = FabPosition.End,


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
        composable(route = DogScreen.SettingsScreen.name) { SettingsScreen(navController) }
        composable(
            route = "DogDetailScreen/{dogId}",
            arguments = listOf(navArgument("dogId") {type = NavType.StringType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId") ?: ""
            DogDetailScreen(navController = navController ,dogIdx = dogId)
        }


    }
}

enum class DogScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    Register(title = R.string.register),
    Home(title = R.string.home),
    Map(title = R.string.map),
    NewUser(title = R.string.newUser),
    AddDog(title = R.string.addDog),
    DogDetail(title = R.string.DogDetail),
    //AddVetLog(title = R.string.addVetLog),
    SettingsScreen(title = R.string.settings)

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
            icon = { Icon(Icons.Default.Home, contentDescription = "Hjem") },
            label = { Text("Hjem") },
            selected = currentDestination == "home",
            onClick = {
                if (currentDestination != "home") {
                    navController.navigate("home")
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = DogScreen.Map.name) },
            label = { Text(text = "Kart") },
            selected = currentDestination == DogScreen.Map.name,
            onClick = {
                if (currentDestination != DogScreen.Map.name) {
                    navController.navigate(route = DogScreen.Map.name)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Innstillinger") },
            label = { Text("Innstillinger") },
            selected = currentDestination == DogScreen.SettingsScreen.name,
            onClick = {
                if (currentDestination != DogScreen.SettingsScreen.name) {
                    navController.navigate(route = DogScreen.SettingsScreen.name)
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
