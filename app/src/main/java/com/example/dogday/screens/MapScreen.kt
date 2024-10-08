package com.example.dogday.screens

import android.os.Bundle
import android.view.LayoutInflater
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.dogday.adapters.CustomMapMarker
import com.example.dogday.models.HikeData
import com.example.dogday.models.Kennel
import com.example.dogday.repository.HikeRepository
import com.example.dogday.repository.KennelRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val kennelRepository = KennelRepository()
    val hikeRepository = HikeRepository()

    // State for holding fetched kennels and hikes
    var kennels by remember { mutableStateOf(listOf<Kennel>()) }
    var hikes by remember { mutableStateOf(listOf<HikeData>()) }

    // State to hold the MapView
    var mapView by remember { mutableStateOf<MapView?>(null) }

    // State for toggles
    var showKennels by remember { mutableStateOf(true) }
    var showHikes by remember { mutableStateOf(false) }

    // Fetch data based on toggles
    LaunchedEffect(showKennels, showHikes) {
        if (showKennels) {
            kennelRepository.fetchKennels(
                onSuccess = { fetchedKennels ->
                    kennels = fetchedKennels
                    updateMapWithMarkers(mapView, context, kennels, hikes, showKennels, showHikes)
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
                    updateMapWithMarkers(mapView, context, kennels, hikes, showKennels, showHikes)
                },
                onFailure = { exception ->
                    // Handle error
                }
            )
        }
    }

    // Lifecycle observer to manage the MapView
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

        // Display the MapView using AndroidView
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    mapView = this
                    getMapAsync { map ->
                        // Set initial camera position
                        val osloLocation = LatLng(59.911491, 10.757933)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(osloLocation, 12f))

                        // Set the custom marker adapter
                        map.setInfoWindowAdapter(CustomMapMarker(LayoutInflater.from(context), map))
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Helper function to update the map with markers
private fun updateMapWithMarkers(
    mapView: MapView?,
    context: android.content.Context,
    kennels: List<Kennel>,
    hikes: List<HikeData>,
    showKennels: Boolean,
    showHikes: Boolean
) {
    mapView?.getMapAsync { map ->
        // Clear previous markers
        map.clear()

        // Add markers for kennels if toggled on
        if (showKennels) {
            kennels.forEach { kennel ->
                val kennelLocation = LatLng(
                    kennel.coordinates.latitude,
                    kennel.coordinates.longitude
                )
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(kennelLocation)
                        .title(kennel.name)
                        .snippet("${kennel.address}\nContact: ${kennel.contactInfo}")
                )
                marker?.tag = kennel
            }
        }

        // Add markers for hikes if toggled on
        if (showHikes) {
            hikes.forEach { hike ->
                val hikeLocation = LatLng(
                    hike.coordinates.latitude,
                    hike.coordinates.longitude
                )
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(hikeLocation)
                        .title(hike.name)
                        .snippet(hike.description)
                )
                marker?.tag = hike
            }
        }
    }
}
