package com.example.dogday.screens

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.dogday.util.seedDogRecommendations
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        // Initialize Firebase Analytics
        analytics = Firebase.analytics

        // Log an event
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)


        setContent {
            MyAppTest01Theme{
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentScreen = when {
        currentRoute == null -> DogScreen.Login
        currentRoute.startsWith("DogDetailScreen") -> DogScreen.DogDetail
        currentRoute.startsWith("kennel_detail") -> DogScreen.KennelDetail
        currentRoute.startsWith("hike_detail") -> DogScreen.HikeDetail
        else -> DogScreen.values().find { it.name == currentRoute } ?: DogScreen.Login
    }

    val noBarRoutes = listOf(
        DogScreen.AddDog.name,
        DogScreen.NewUser.name,
        DogScreen.Register.name,
        DogScreen.Login.name,
        DogScreen.DogQueryScreen.name,
        "kennel_detail",  // Add if you want to hide bars on these screens
        "hike_detail",
    )



    Scaffold(
        topBar = {
            if (!noBarRoutes.contains(currentRoute)){
                DogAppBar(
                    canNavigateBack = navController.previousBackStackEntry != null && currentRoute != DogScreen.Home.name,
                    navigateUp = {navController.navigateUp()},
                    currentScreen = currentScreen
                )
            }
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
            if (!noBarRoutes.contains(currentRoute)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavigationHost(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier) {


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
        composable(route = DogScreen.DogQueryScreen.name) { DogQueryScreen(navController) }
        composable(route = DogScreen.UserDogScreen.name) { UserDogScreen(navController) }
        composable(route = DogScreen.Quiz.name) { DogQuizScreen(navController) }
        composable("quiz_results/{dogID}") { backStackEntry ->
            val dogID = backStackEntry.arguments?.getString("dogID") ?: DogID.BULLDOG.name
            DogQuizResultsScreen(navController = navController, dogID = dogID)
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
    QuizResults(title = R.string.quizresults)

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
            icon = { Icon(Icons.Default.Home, contentDescription = "Hjem") },
            label = { Text("Hjem") },
            selected = currentDestination == DogScreen.Home.name,
            onClick = {
                if (currentDestination != DogScreen.Home.name) {
                    navController.navigate(DogScreen.Home.name)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Kart") },
            label = { Text("Kart") },
            selected = currentDestination == DogScreen.Map.name,
            onClick = {
                if (currentDestination != DogScreen.Map.name) {
                    navController.navigate(DogScreen.Map.name)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Innstillinger") },
            label = { Text("Innstillinger") },
            selected = currentDestination == DogScreen.SettingsScreen.name,
            onClick = {
                if (currentDestination != DogScreen.SettingsScreen.name) {
                    navController.navigate(DogScreen.SettingsScreen.name)
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
