package com.example.dogday

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen(navController: NavHostController) {
    // Coordinates for the test pin (for example, somewhere in Oslo, Norway)
    val testLocation = LatLng(59.911491, 10.757933)

    // Camera position state to control the map's initial position
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(testLocation, 12f)
    }

    // Google Map Composable
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Adding a marker (test pin) to the map
        Marker(
            state = com.google.maps.android.compose.MarkerState(position = testLocation),
            title = "Test Pin",
            snippet = "This is a test pin in Oslo, Norway"
        )
    }
}
