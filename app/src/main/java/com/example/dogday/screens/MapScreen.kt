package com.example.dogday.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.dogday.adapters.CustomMapMarker
import com.example.dogday.models.HikeData
import com.example.dogday.models.Kennel
import com.example.dogday.repository.HikeRepository
import com.example.dogday.repository.KennelRepository
import com.example.dogday.ui.widgets.ItemSlider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val kennelRepository = KennelRepository()
    val hikeRepository = HikeRepository()

    // State variables
    var kennels by remember { mutableStateOf(listOf<Kennel>()) }
    var hikes by remember { mutableStateOf(listOf<HikeData>()) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var showKennels by remember { mutableStateOf(true) }
    var showHikes by remember { mutableStateOf(false) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var visibleItems by remember { mutableStateOf(listOf<Any>()) }

    // Permission handling
    var hasLocationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Update the state based on whether permissions were granted
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (hasLocationPermission) {
            // Permissions granted, update the map
            googleMap?.let { map ->
                try {
                    map.isMyLocationEnabled = true
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        // Check if permissions are already granted
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        hasLocationPermission = fineLocationPermission || coarseLocationPermission

        if (!hasLocationPermission) {
            // Request permissions
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Fetch data based on toggles
    LaunchedEffect(showKennels, showHikes) {
        if (showKennels) {
            kennelRepository.fetchKennels(
                onSuccess = { fetchedKennels ->
                    kennels = fetchedKennels
                    updateMapWithMarkers(googleMap, kennels, hikes, showKennels, showHikes)
                },
                onFailure = { exception ->
                    // Handle error
                }
            )
        }

        if (showHikes) {
            hikeRepository.fetchHikeLocations(
                onSuccess = { fetchedHikes ->
                    hikes = fetchedHikes
                    updateMapWithMarkers(googleMap, kennels, hikes, showKennels, showHikes)
                },
                onFailure = { exception ->
                    // Handle error
                }
            )
        }
    }

    // Update markers when data or toggles change
    LaunchedEffect(googleMap, kennels, hikes, showKennels, showHikes) {
        updateMapWithMarkers(googleMap, kennels, hikes, showKennels, showHikes)
    }

    // Lifecycle observer
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
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
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Column {
        // Row for toggles
        Row {
            Text("Show Kennels")
            Switch(
                checked = showKennels,
                onCheckedChange = { showKennels = it }
            )
            Text("Show Hikes")
            Switch(
                checked = showHikes,
                onCheckedChange = { showHikes = it }
            )
        }

        // Use Box to overlay the ItemSlider on the MapView
        Box(modifier = Modifier.fillMaxSize()) {
            // Display the MapView using AndroidView
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        mapView = this
                        getMapAsync { map ->
                            googleMap = map

                            if (hasLocationPermission) {
                                try {
                                    map.isMyLocationEnabled = true
                                } catch (e: SecurityException) {
                                    e.printStackTrace()
                                }
                            }

                            // Set initial camera position
                            val osloLocation = LatLng(59.911491, 10.757933)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(osloLocation, 12f))

                            // Set the custom marker adapter
                            map.setInfoWindowAdapter(
                                CustomMapMarker(LayoutInflater.from(context), map)
                            )

                            // Set up OnCameraIdleListener
                            map.setOnCameraIdleListener {
                                val visibleRegion = map.projection.visibleRegion.latLngBounds
                                val itemsInBounds = mutableListOf<Any>()

                                if (showKennels) {
                                    itemsInBounds.addAll(
                                        kennels.filter {
                                            visibleRegion.contains(
                                                LatLng(
                                                    it.coordinates.latitude,
                                                    it.coordinates.longitude
                                                )
                                            )
                                        }
                                    )
                                }

                                if (showHikes) {
                                    itemsInBounds.addAll(
                                        hikes.filter {
                                            visibleRegion.contains(
                                                LatLng(
                                                    it.coordinates.latitude,
                                                    it.coordinates.longitude
                                                )
                                            )
                                        }
                                    )
                                }

                                visibleItems = itemsInBounds
                            }

                            // Update the map with markers initially
                            updateMapWithMarkers(map, kennels, hikes, showKennels, showHikes)
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Overlay the ItemSlider at the bottom
            ItemSlider(
                visibleItems = visibleItems,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color(0xAAFFFFFF)) // Semi-transparent background
            )
        }
    }
}

// Helper function to update the map with markers
private fun updateMapWithMarkers(
    map: GoogleMap?,
    kennels: List<Kennel>,
    hikes: List<HikeData>,
    showKennels: Boolean,
    showHikes: Boolean
) {
    map?.let { googleMap ->
        CoroutineScope(Dispatchers.Main).launch {
            // Clear previous markers
            googleMap.clear()

            withContext(Dispatchers.Default) {
                val markers = mutableListOf<MarkerOptions>()

                if (showKennels) {
                    markers.addAll(kennels.map { kennel ->
                        MarkerOptions()
                            .position(LatLng(kennel.coordinates.latitude, kennel.coordinates.longitude))
                            .title(kennel.name)
                            .snippet("${kennel.address}\nContact: ${kennel.contactInfo}")
                    })
                }

                if (showHikes) {
                    markers.addAll(hikes.map { hike ->
                        MarkerOptions()
                            .position(LatLng(hike.coordinates.latitude, hike.coordinates.longitude))
                            .title(hike.name)
                            .snippet(hike.description)
                    })
                }

                // Add markers on the main thread
                withContext(Dispatchers.Main) {
                    markers.forEach { markerOptions ->
                        val marker = googleMap.addMarker(markerOptions)
                        // Assign tag if needed
                    }
                }
            }
        }
    }
}

