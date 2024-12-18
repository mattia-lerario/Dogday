package com.example.dogday.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.dogday.models.Breeder
import com.example.dogday.models.HikeData
import com.example.dogday.models.Kennel
import com.example.dogday.repository.BreederRepository
import com.example.dogday.repository.HikeRepository
import com.example.dogday.repository.KennelRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint

class MapViewModel : ViewModel() {

    private val kennelRepository = KennelRepository()
    private val hikeRepository = HikeRepository()
    private val breederRepository = BreederRepository()

    var kennels by mutableStateOf<List<Kennel>>(emptyList())
        private set

    var hikes by mutableStateOf<List<HikeData>>(emptyList())
        private set

    var breeders by mutableStateOf<List<Breeder>>(emptyList())
        private set


    var showKennels by mutableStateOf(false)
        private set

    var showHikes by mutableStateOf(false)
        private set

    var showBreeders by mutableStateOf(false)
        private set


    var visibleItems by mutableStateOf<List<Any>>(emptyList())
        private set

    var hasLocationPermission by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var showAddHikeDialog by mutableStateOf(false)
        private set

    var hikeTitle by mutableStateOf("")
        private set

    var hikeDescription by mutableStateOf("")
        private set

    var hikeAddress by mutableStateOf("")
        private set

    var hikeImageUrl by mutableStateOf("")
        private set

    var hikeCoordinates by mutableStateOf(GeoPoint(59.911491, 10.757933))
        private set


    fun updateShowKennels(show: Boolean) {
        showKennels = show
        fetchKennels()
    }

    fun updateShowHikes(show: Boolean) {
        showHikes = show
        fetchHikes()
    }

    fun updateShowBreeders(show: Boolean) {
        showBreeders = show
        fetchBreeders()
    }
    fun openAddHikeDialog() {
        showAddHikeDialog = true
    }

    fun closeAddHikeDialog() {
        showAddHikeDialog = false
    }


    fun onHikeTitleChange(newTitle: String) {
        hikeTitle = newTitle
    }

    fun onHikeDescriptionChange(newDescription: String) {
        hikeDescription = newDescription
    }

    fun onHikeAddressChange(newAddress: String) {
        hikeAddress = newAddress
    }

    fun onHikeCoordinatesChange(newCoordinates: GeoPoint) {
        hikeCoordinates = newCoordinates
    }


    fun onHikeImageSelected(imageUrl: String) {
        hikeImageUrl = imageUrl
    }


    fun addNewHike() {
        val newHike = HikeData(
            id = "",
            title = hikeTitle,
            coordinates = hikeCoordinates,
            description = hikeDescription,
            imageUrl = hikeImageUrl,
            address = hikeAddress
        )
        addHike(newHike)
        closeAddHikeDialog()
    }


    private fun fetchKennels() {
        if (showKennels) {
            kennelRepository.fetchKennels(
                onSuccess = { fetchedKennels ->
                    kennels = fetchedKennels
                    errorMessage = null
                },
                onFailure = { _ ->
                    Log.e("MapViewModel", "Error fetching kennels")
                    errorMessage = "Failed to load kennels. Please try again."
                }
            )
        } else {
            kennels = emptyList()
        }
    }

    private fun fetchHikes() {
        if (showHikes) {
            hikeRepository.fetchHikeLocations(
                onSuccess = { fetchedHikes ->
                    hikes = fetchedHikes
                    errorMessage = null
                },
                onFailure = { _ ->
                    Log.e("MapViewModel", "Error fetching hikes")
                    errorMessage = "Failed to load hikes. Please try again."
                }
            )
        } else {
            hikes = emptyList()
        }
    }

    private fun fetchBreeders() {
        if (showBreeders) {
            breederRepository.fetchBreeders(
                onSuccess = { fetchedBreeders ->
                    breeders = fetchedBreeders
                    errorMessage = null // Clear error message on successful fetch
                    Log.d("MapViewModel", "Fetched breeders: $fetchedBreeders")
                },
                onFailure = { _ ->
                    Log.e("MapViewModel", "Error fetching breeders")
                    errorMessage = "Failed to load breeders. Please try again."
                }
            )
        } else {
            breeders = emptyList()
            Log.d("MapViewModel", "Breeders list cleared")
        }
    }

    /**
     * Adds markers for kennels, hikes, and breeders to the GoogleMap instance.
     * Clears the map before adding new markers based on the visibility toggles for each type of item.
     * Uses different colors for markers to differentiate the categories.
     */

    fun updateMapWithMarkers(map: GoogleMap) {
        map.clear()

        if (showKennels) {
            kennels.forEach { kennel ->
                kennel.coordinates?.let { coordinates ->
                    val markerOptions = MarkerOptions()
                        .position(LatLng(coordinates.latitude, coordinates.longitude))
                        .title(kennel.name ?: "Unknown Kennel")
                        .snippet("${kennel.address}\nContact: ${kennel.contactInfo ?: "No contact info"}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)) // Kennel marker in orange

                    map.addMarker(markerOptions)
                }
            }
        }

        if (showHikes) {
            hikes.forEach { hike ->
                hike.coordinates.let { coordinates ->
                    val markerOptions = MarkerOptions()
                        .position(LatLng(coordinates.latitude, coordinates.longitude))
                        .title(hike.title)
                        .snippet(hike.description)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) // Hike marker in green

                    map.addMarker(markerOptions)
                }
            }
        }

        if (showBreeders) {
            breeders.forEach { breeder ->
                breeder.coordinates?.let { coordinates ->
                    val markerOptions = MarkerOptions()
                        .position(LatLng(coordinates.latitude, coordinates.longitude))
                        .title(breeder.name ?: "Unknown Breeder")
                        .snippet(breeder.address ?: "Address not available")  // Simplified snippet to avoid issues
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))  // Breeder marker in blue

                    map.addMarker(markerOptions)?.let {
                        Log.d("MapViewModel", "Added breeder marker for: ${breeder.name}")
                    } ?: run {
                        Log.e("MapViewModel", "Failed to add breeder marker for: ${breeder.name}")
                    }
                }
            }
        }
    }


    /**
     * Updates the list of visible items based on the current visible map region.
     * Filters kennels, hikes, and breeders to see which ones fall within the given LatLngBounds.
     */
    fun updateVisibleItems(visibleRegion: LatLngBounds) {
        val itemsInBounds = mutableListOf<Any>()

        if (showKennels) {
            val kennelsInBounds = kennels.filter {
                it.coordinates?.let { geoPoint ->
                    visibleRegion.contains(LatLng(geoPoint.latitude, geoPoint.longitude))
                } ?: false
            }
            itemsInBounds.addAll(kennelsInBounds)
            Log.d("MapViewModel", "Kennels in bounds: $kennelsInBounds")
        }

        if (showHikes) {
            val hikesInBounds = hikes.filter {
                it.coordinates.let { geoPoint ->
                    visibleRegion.contains(LatLng(geoPoint.latitude, geoPoint.longitude))
                }
            }
            itemsInBounds.addAll(hikesInBounds)
            Log.d("MapViewModel", "Hikes in bounds: $hikesInBounds")
        }

        if (showBreeders) {
            val breedersInBounds = breeders.filter {
                it.coordinates?.let { geoPoint ->
                    visibleRegion.contains(LatLng(geoPoint.latitude, geoPoint.longitude))
                } ?: false
            }
            itemsInBounds.addAll(breedersInBounds)
            Log.d("MapViewModel", "Breeders in bounds: $breedersInBounds")
        }

        visibleItems = itemsInBounds
        Log.d("MapViewModel", "Visible items updated: $visibleItems")
    }

    /**
     * Centers the map on markers for kennels, hikes, or breeders, or uses a default location.
     * Adjusts the camera position to fit all visible markers, with a fallback to a default zoomed position.
     */
    fun centerMapOnMarkersOrDefault(googleMap: GoogleMap, mapViewModel: MapViewModel) {
        if (mapViewModel.kennels.isNotEmpty() || mapViewModel.hikes.isNotEmpty() || mapViewModel.breeders.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()

            if (mapViewModel.showKennels) {
                mapViewModel.kennels.forEach { kennel ->
                    kennel.coordinates?.let { coordinates ->
                        boundsBuilder.include(LatLng(coordinates.latitude, coordinates.longitude))
                    }
                }
            }

            if (mapViewModel.showHikes) {
                mapViewModel.hikes.forEach { hike ->
                    hike.coordinates.let { coordinates ->
                        boundsBuilder.include(LatLng(coordinates.latitude, coordinates.longitude))
                    }
                }
            }

            if (mapViewModel.showBreeders) {
                mapViewModel.breeders.forEach { breeder ->
                    breeder.coordinates?.let { coordinates ->
                        boundsBuilder.include(LatLng(coordinates.latitude, coordinates.longitude))
                    }
                }
            }

            try {
                if (mapViewModel.kennels.size + mapViewModel.hikes.size + mapViewModel.breeders.size > 1) {
                    val bounds = boundsBuilder.build()
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                    googleMap.moveCamera(cameraUpdate)
                } else {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(59.911491, 10.757933), 12f))
                }
            } catch (e: Exception) {
                Log.e("MapScreen", "Error adjusting camera: ${e.message}")
            }
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(59.911491, 10.757933), 12f))
        }
    }


    /**
     * Checks if the app has the required location permissions.
     * Updates the hasLocationPermission flag and returns whether the permission is granted.
     */
    fun checkLocationPermission(context: Context): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        hasLocationPermission = fineLocationPermission || coarseLocationPermission
        return hasLocationPermission
    }


    /**
     * Adds a new hike to the repository and updates the state to include it in the hikes list.
     */
    fun addHike(hike: HikeData) {
        hikeRepository.addHike(
            hike,
            onSuccess = {
                hikes = hikes + hike
            },
            onFailure = { exception ->
                Log.e("MapViewModel", "Error adding hike: ${exception.message}")
            }
        )
    }

    /**
     * Toggles the visibility state of kennels, hikes, or breeders based on the provided label.
     */
    fun toggleShowState(label: String) {
        when (label) {
            "Kennels" -> {
                updateShowKennels(!showKennels)
            }
            "Hikes" -> {
                updateShowHikes(!showHikes)
            }
            "Breeders" -> {
                updateShowBreeders(!showBreeders)
            }
            else -> {
                Log.e("MapViewModel", "Invalid label: $label")
            }
        }
    }

    /**
     * Converts a vector resource into a BitmapDescriptor to use as a custom marker icon on the map.
     * Useful for using drawable resources as map markers.
     * THIS FUNCTION IS NOT IN USE AS PART OF THE CUSTOM MAP MARKER FILES.
     */
    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable: Drawable? = ContextCompat.getDrawable(context, vectorResId)
        if (vectorDrawable == null) {
            Log.e("MapViewModel", "Resource not found: $vectorResId")
            return null
        }

        vectorDrawable.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
