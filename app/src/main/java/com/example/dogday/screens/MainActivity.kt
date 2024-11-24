package com.example.dogday.screens

import DogListViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogday.R
import com.example.dogday.models.DogID
import com.example.dogday.ui.theme.MyAppTest01Theme
import com.google.android.libraries.places.api.Places
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth


// Inside MainActivity.kt
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        // Initialize Google Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "YOUR_API_KEY")
        }
        // Initialize Firebase Analytics
        analytics = Firebase.analytics

        // Log an event to verify Firebase initialization
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
        Log.d("MainActivity", "Firebase initialized successfully")

        setContent {
            MyAppTest01Theme {
                MainApp(context = applicationContext)
            }
        }
    }
}


@Composable
fun MainApp(context: Context) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val firebaseAuth = FirebaseAuth.getInstance()

    val sharedPreferences = context.getSharedPreferences("dogday_preferences", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

    // Check if user is authenticated and decide the initial screen
    LaunchedEffect(Unit) {
        if (firebaseAuth.currentUser != null && isLoggedIn) {
            // Log the current user state
            Log.d("MainApp", "User already logged in, navigating to Home")
            // User is authenticated, navigate to Home screen
            if (currentRoute == null || currentRoute == DogScreen.Login.name) {
                navController.navigate(DogScreen.Home.name) {
                    popUpTo(0) // Clear all backstack to prevent navigating back to login
                }
            }
        } else {
            // Log the current user state
            Log.d("MainApp", "User not logged in, navigating to Login")
            // User is not authenticated, navigate to Login screen
            if (currentRoute != DogScreen.Login.name) {
                navController.navigate(DogScreen.Login.name) {
                    popUpTo(0) // Clear all backstack to ensure a fresh start
                }
            }
        }
    }

    val currentScreen = when {
        firebaseAuth.currentUser != null && isLoggedIn -> DogScreen.Home
        currentRoute == null -> DogScreen.Login
        currentRoute.startsWith("DogDetailScreen") -> DogScreen.DogDetail
        currentRoute.startsWith("kennel_detail") -> DogScreen.KennelDetail
        currentRoute.startsWith("hike_detail") -> DogScreen.HikeDetail
        currentRoute.startsWith("dog_list") -> DogScreen.RecommendedDogListScreen
        currentRoute.startsWith("dog_detail") -> DogScreen.RecommendedDogDetailScreen
        currentRoute.startsWith("quiz_results") -> DogScreen.DogQuizResultsScreen
        else -> DogScreen.values().find { it.name == currentRoute } ?: DogScreen.Login
    }

    val noBarRoutes = listOf(
        DogScreen.AddDog.name,
        DogScreen.NewUser.name,
        DogScreen.Register.name,
        DogScreen.Login.name,
        DogScreen.DogQueryScreen.name,
        "kennel_detail",
        "hike_detail",
    )

    Scaffold(
        topBar = {
            if (!noBarRoutes.contains(currentRoute)) {
                DogAppBar(
                    canNavigateBack = navController.previousBackStackEntry != null && currentRoute != DogScreen.Home.name,
                    navigateUp = { navController.navigateUp() },
                    currentScreen = currentScreen
                )
            }
        },
        floatingActionButton = {
            if (currentScreen == DogScreen.DogDetail) {
                val dogId = backStackEntry?.arguments?.getString("dogId") ?: ""
                FloatingActionButton(
                    onClick = { navController.navigate("addVetNote/$dogId") },
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (!noBarRoutes.contains(currentRoute)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavigationHost(navController = navController, modifier = Modifier.padding(paddingValues), context = context)
    }
}




@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier, context: Context) {
    val viewModel: DogListViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable(DogScreen.Login.name) {
            LoginScreen(navController, context)   // Pass context to LoginScreen here
        }
        composable(DogScreen.Home.name) {
            HomeScreen(navController)
        }
        composable(DogScreen.Map.name) {
            MapScreen(navController)
        }
        composable(DogScreen.Register.name) {
            RegisterScreen(navController)
        }
        composable(DogScreen.NewUser.name) {
            NewUserScreen(navController)
        }
        composable(DogScreen.AddDog.name) {
            AddDogScreen(navController)
        }
        composable(route = DogScreen.SettingsScreen.name) {
            SettingsScreen(navController)
        }
        composable(
            route = "DogDetailScreen/{dogId}",
            arguments = listOf(navArgument("dogId") { type = NavType.StringType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId") ?: ""
            DogDetailScreen(navController = navController, dogIdx = dogId)
        }
        composable(
            route = "kennel_detail/{kennelId}",
            arguments = listOf(navArgument("kennelId") { type = NavType.StringType })
        ) { backStackEntry ->
            val kennelId = backStackEntry.arguments?.getString("kennelId") ?: ""
            KennelDetailScreen(kennelId = kennelId)
        }
        composable(
            route = "hike_detail/{hikeId}",
            arguments = listOf(navArgument("hikeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val hikeId = backStackEntry.arguments?.getString("hikeId") ?: ""
            HikeDetailScreen(hikeId = hikeId)
        }
        composable(
            route = "breeder_detail/{breederId}",
            arguments = listOf(navArgument("breederId") { type = NavType.StringType })
        ) { backStackEntry ->
            val breederId = backStackEntry.arguments?.getString("breederId") ?: ""
            BreederDetailScreen(breederId = breederId)
        }
        composable(route = DogScreen.DogQueryScreen.name) {
            DogQueryScreen(navController)
        }

        composable(
            route = "addVetNote/{dogId}",
            arguments = listOf(navArgument("dogId") { type = NavType.StringType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId") ?: ""
            VetNoteScreen(
                navController = navController,
                dogId = dogId,
                onSaveNote = { vetNote ->
                    viewModel.fetchDog(dogId)
                    viewModel.dog.value?.let { dog ->
                        if (dog.vetLog.any { it.id == vetNote.id }) {
                            viewModel.updateVetNoteForDog(dog, vetNote)
                        } else {
                            viewModel.addNoteToDog(dog, vetNote, onSuccess = {
                                viewModel.fetchDog(dogId)
                                navController.popBackStack()
                            }, onFailure = { exception ->
                                println("Error saving note: ${exception.message}")
                            })
                        }
                    }
                },
                onDeleteNote = { vetNote ->
                    viewModel.fetchDog(dogId)
                    viewModel.dog.value?.let { dog ->
                        viewModel.deleteVetNoteForDog(dog, vetNote)
                        navController.popBackStack()
                    }
                }
            )
        }
        composable("editDog/{dogId}", arguments = listOf(navArgument("dogId") { type = NavType.StringType })) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId") ?: ""
            viewModel.fetchDog(dogId)
            val dog by viewModel.dog.collectAsState()

            dog?.let {
                EditDogScreen(
                    navController = navController,
                    dog = it,
                    onSave = { updatedDog -> viewModel.updateDog(updatedDog) },
                    onDelete = { viewModel.deleteDog(it) }
                )
            }
        }

        composable(route = DogScreen.UserDogScreen.name) {
            UserDogScreen(navController)
        }
        composable(route = DogScreen.Quiz.name) {
            DogQuizScreen(navController)
        }
        composable("quiz_results/{dogID}") { backStackEntry ->
            val dogID = backStackEntry.arguments?.getString("dogID") ?: DogID.BULLDOG.name
            DogQuizResultsScreen(navController = navController, dogID = dogID)
        }

        composable("dog_list") {
            RecommendedDogListScreen(navController)
        }
        composable("dog_detail/{breed}", arguments = listOf(navArgument("breed") { type = NavType.StringType })) { backStackEntry ->
            val breed = backStackEntry.arguments?.getString("breed") ?: ""
            RecommendedDogDetailScreen(navController, breed)
        }
    }
}


enum class DogScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    Register(title = R.string.register),
    Home(title = R.string.home),
    Map(title = R.string.map),
    NewUser(title = R.string.new_user),
    AddDog(title = R.string.add_dog),
    DogDetail(title = R.string.dog_detail),
    SettingsScreen(title = R.string.settings),
    DogQueryScreen(title = R.string.dog_query_screen),
    UserDogScreen(title = R.string.user_dog_screen),
    KennelDetail(title = R.string.kennel_detail),
    HikeDetail(title = R.string.hike_detail),
    Quiz(title = R.string.quiz),
    DogQuizResultsScreen(title = R.string.quiz_results),
    AddVetNote(title = R.string.add_note),
    RecommendedDogListScreen(title = R.string.recommended_dog_list),
    RecommendedDogDetailScreen(title = R.string.recommended_dog_detail)

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogAppBar(
    currentScreen: DogScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            }
        )
    }
}




@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentDestination = navController.currentDestination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentDestination == DogScreen.Home.name,
            onClick = {
                if (currentDestination != DogScreen.Home.name) {
                    navController.navigate(DogScreen.Home.name)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Map") },
            label = { Text("Map") },
            selected = currentDestination == DogScreen.Map.name,
            onClick = {
                if (currentDestination != DogScreen.Map.name) {
                    navController.navigate(DogScreen.Map.name)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentDestination == DogScreen.SettingsScreen.name,
            onClick = {
                if (currentDestination != DogScreen.SettingsScreen.name) {
                    navController.navigate(DogScreen.SettingsScreen.name)
                }
            }
        )
    }
}

