package com.example.dogday

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.dogday.LoginScreen // Import the composable function
import com.example.dogday.RegisterScreen // Import the composable function
import com.example.dogday.ui.screens.MapScreenWithKennels
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp


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

enum class DogScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    Register(title = R.string.register),
    Home(title = R.string.home),
    Map(title = R.string.map),
    NewUser(title = R.string.newUser),
    AddDog(title = R.string.addDog),
    DogDetail(title = R.string.DogDetail),
    AddVetLog(title = R.string.addVetLog)
    //DogDetail/{dogId}
    //Summary(title = R.string.order_summary)

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



@Composable
fun MainApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    //val currentScreen = DogScreen.valueOf(backStackEntry?.destination?.route ?: DogScreen.Login.name)
    val currentRoute = backStackEntry?.destination?.route
    val currentScreen = when {
        currentRoute?.startsWith("DogDetailScreen") == true -> DogScreen.DogDetail
        else -> DogScreen.valueOf(currentRoute ?: DogScreen.Login.name)
    }

    Scaffold(
        topBar = {
            DogAppBar(
                canNavigateBack = true,
                navigateUp = {},
                currentScreen = currentScreen
            )
        },


        floatingActionButton = {
            if (currentScreen == DogScreen.DogDetail){
            FloatingActionButton(
                onClick = { },
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

    NavHost(navController = navController, startDestination = DogScreen.Login.name, modifier = modifier) {
        composable(route = DogScreen.Login.name) { LoginScreen(navController) }   // Use LoginScreen here
        composable(route = DogScreen.Home.name) { HomeScreen(navController) }      // Home Screen of the application
        composable(route = DogScreen.Map.name) { MapScreenWithKennels(navController) }        // Map Screen
        composable(route = DogScreen.Register.name) { RegisterScreen(navController) } // Use RegisterScreen here
        composable(route = DogScreen.NewUser.name) {NewUserScreen(navController)} // No need to pass uid and email
        composable(route = DogScreen.AddDog.name) { AddDogScreen(navController) }
        composable(route = DogScreen.AddVetLog.name) {addVetLogScreen(navController)}
        composable(
            route = "DogDetailScreen/{dogId}",
            arguments = listOf(navArgument("dogId") {type = NavType.StringType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId") ?: ""
            DogDetailScreen(navController = navController, dogIdx = dogId)

    }
}


@Composable
fun DefaultPreview() {
    MainApp()
}}
