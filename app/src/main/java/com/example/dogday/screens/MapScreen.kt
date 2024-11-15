package com.example.dogday.screens

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapViewModel: MapViewModel = viewModel()

    var mapView by remember { mutableStateOf<MapView?>(null) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Permission handling (same as before)
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

    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView?.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView?.onStart()
                Lifecycle.Event.ON_RESUME -> mapView?.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView?.onPause()
                Lifecycle.Event.ON_STOP -> mapView?.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView?.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycleOwner.lifecycle.removeObserver(lifecycleObserver) }
    }

    // Center the camera on the user's location or on the markers if available
    LaunchedEffect(googleMap, mapViewModel.kennels, mapViewModel.hikes, mapViewModel.breeders) {
        if (googleMap != null) {
            if (mapViewModel.hasLocationPermission) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 6f))
                    } else {
                        // If user location is not available, move to the markers
                        mapViewModel.centerMapOnMarkersOrDefault(googleMap!!, mapViewModel)
                    }
                }.addOnFailureListener {
                    // If fetching the user location fails, move to the markers
                    mapViewModel.centerMapOnMarkersOrDefault(googleMap!!, mapViewModel)
                }
            } else {
                // If location permission is not granted, center map on the markers or default location
                mapViewModel.centerMapOnMarkersOrDefault(googleMap!!, mapViewModel)
            }
        }
    }

    Column {
        if (mapViewModel.errorMessage != null) {
            Text(
                text = mapViewModel.errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    Column {
        Row {
            Text("Show Kennels")
            Switch(
                checked = mapViewModel.showKennels,
                onCheckedChange = { mapViewModel.updateShowKennels(it) }
            )
            Text("Show Hikes")
            Switch(
                checked = mapViewModel.showHikes,
                onCheckedChange = { mapViewModel.updateShowHikes(it) }
            )
            Text("Show Breeders")
            Switch(
                checked = mapViewModel.showBreeders,
                onCheckedChange = { mapViewModel.updateShowBreeders(it) }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Display the MapView using AndroidView
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        mapView = this
                        getMapAsync { map ->
                            googleMap = map

                            if (mapViewModel.hasLocationPermission) {
                                try {
                                    map.isMyLocationEnabled = true
                                } catch (e: SecurityException) {
                                    e.printStackTrace()
                                }
                            }

                            // Set up OnCameraIdleListener to update visible items
                            map.setOnCameraIdleListener {
                                mapViewModel.updateVisibleItems(map.projection.visibleRegion.latLngBounds)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Use LaunchedEffect to update the map with markers when googleMap is ready
            LaunchedEffect(googleMap, mapViewModel.kennels, mapViewModel.hikes, mapViewModel.breeders, mapViewModel.showKennels, mapViewModel.showHikes, mapViewModel.showBreeders) {
                if (googleMap != null) {
                    mapViewModel.updateMapWithMarkers(googleMap!!, context)
                }
            }

            // Display the ItemSlider at the bottom
            ItemSlider(
                visibleItems = mapViewModel.visibleItems,
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter).background(Color(0xAAFFFFFF)) // Semi-transparent background
            )
        }
    }
}