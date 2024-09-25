package com.example.dogday.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.dogday.models.Kennel
import com.example.dogday.repository.KennelRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


@Composable
fun MapScreenWithKennels(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val kennelRepository = KennelRepository()

    // State for holding fetched kennels
    var kennels by remember { mutableStateOf(listOf<Kennel>()) }

    // State to hold the MapView
    var mapView by remember { mutableStateOf<MapView?>(null) }

    // Fetch the kennels when the composable is displayed
    LaunchedEffect(Unit) {
        kennelRepository.fetchKennels(
            onSuccess = { fetchedKennels ->
                kennels = fetchedKennels
                mapView?.getMapAsync { map ->
                    // Set the custom info window adapter
                    map.setInfoWindowAdapter(CustomMapMarker(LayoutInflater.from(context)))

                    // Update the map with the fetched kennel markers
                    kennels.forEach { kennel ->
                        val kennelLocation = LatLng(kennel.coordinates.latitude, kennel.coordinates.longitude)
                        val marker = map.addMarker(
                            MarkerOptions()
                                .position(kennelLocation)
                                .title(kennel.name)
                                .snippet("${kennel.address}, Contact: ${kennel.contactInfo}")
                        )
                        // Set kennel data as the tag for the marker
                        marker?.tag = kennel
                    }
                }
            },
            onFailure = { exception ->
                // Handle error
            }
        )
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

    // Display the MapView using AndroidView
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                mapView = this
                getMapAsync { map ->
                    // Set initial camera position
                    val osloLocation = LatLng(59.911491, 10.757933)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(osloLocation, 12f))
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

