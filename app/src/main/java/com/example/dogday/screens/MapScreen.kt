package com.example.dogday.screens

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dogday.ui.widgets.ItemSlider
import com.example.dogday.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val mapViewModel: MapViewModel = viewModel()

    // Remember the MapView with lifecycle support
    val mapView = rememberMapViewWithLifecycle()
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var showAddHikeDialog by remember { mutableStateOf(false) }

    // Define the launcher to pick images from the user's library
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            mapViewModel.onHikeImageSelected(uri.toString())
        }
    }

    // Permission handling
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        mapViewModel.hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        googleMap?.isMyLocationEnabled = mapViewModel.hasLocationPermission
    }

    LaunchedEffect(Unit) {
        if (!mapViewModel.checkLocationPermission(context)) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Center the camera on the user's location or on the markers if available
    LaunchedEffect(googleMap, mapViewModel.kennels, mapViewModel.hikes, mapViewModel.breeders) {
        googleMap?.let { map ->
            if (mapViewModel.hasLocationPermission) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 6f))
                    } else {
                        mapViewModel.centerMapOnMarkersOrDefault(map, mapViewModel)
                    }
                }.addOnFailureListener {
                    mapViewModel.centerMapOnMarkersOrDefault(map, mapViewModel)
                }
            } else {
                mapViewModel.centerMapOnMarkersOrDefault(map, mapViewModel)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Toggle Buttons Row
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Toggle buttons for kennels, hikes, breeders
            val toggleButtons = listOf(
                "Kennels" to mapViewModel.showKennels,
                "Hikes" to mapViewModel.showHikes,
                "Breeders" to mapViewModel.showBreeders
            )

            toggleButtons.forEach { (label, showState) ->
                Button(
                    onClick = {
                        when (label) {
                            "Kennels" -> mapViewModel.updateShowKennels(!showState)
                            "Hikes" -> mapViewModel.updateShowHikes(!showState)
                            "Breeders" -> mapViewModel.updateShowBreeders(!showState)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showState) Color(0xFFFFB74D) else Color(0xFFFFCC80),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    border = if (showState) BorderStroke(2.dp, Color(0xFFD95A3C)) else null
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.padding(vertical = 4.dp),
                        maxLines = 1
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Display the MapView using AndroidView
            AndroidView(
                factory = {
                    mapView.apply {
                        getMapAsync { map ->
                            googleMap = map

                            if (mapViewModel.hasLocationPermission) {
                                try {
                                    map.isMyLocationEnabled = true
                                } catch (e: SecurityException) {
                                    e.printStackTrace()
                                }
                            }

                            map.setOnCameraIdleListener {
                                mapViewModel.updateVisibleItems(map.projection.visibleRegion.latLngBounds)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // FloatingActionButton for adding hike
            FloatingActionButton(
                onClick = { showAddHikeDialog = true },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                containerColor = Color(0xFFFFB74D)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Hike",
                    tint = Color.Black
                )
            }

            // Update the map with markers when googleMap or other parameters change
            LaunchedEffect(googleMap, mapViewModel.kennels, mapViewModel.hikes, mapViewModel.breeders, mapViewModel.showKennels, mapViewModel.showHikes, mapViewModel.showBreeders) {
                googleMap?.let {
                    mapViewModel.updateMapWithMarkers(it)
                }
            }

            // Display the ItemSlider at the bottom
            ItemSlider(
                visibleItems = mapViewModel.visibleItems,
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
            )
        }
    }

    // Add Hike Dialog
    if (showAddHikeDialog) {
        AlertDialog(
            onDismissRequest = { showAddHikeDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    mapViewModel.addNewHike()
                    showAddHikeDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHikeDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Add New Hike") },
            text = {
                Column {
                    TextField(
                        value = mapViewModel.hikeTitle,
                        onValueChange = { mapViewModel.onHikeTitleChange(it) },
                        label = { Text("Hike Title") },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    TextField(
                        value = mapViewModel.hikeDescription,
                        onValueChange = { mapViewModel.onHikeDescriptionChange(it) },
                        label = { Text("Description") },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    TextField(
                        value = mapViewModel.hikeAddress,
                        onValueChange = { mapViewModel.onHikeAddressChange(it) },
                        label = { Text("Address") },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Select Image")
                    }
                }
            }
        )
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }

        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    return mapView
}
