package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint
import java.util.UUID

data class HikeData(
    val hikeID: String = UUID.randomUUID().toString(),  // Generate a random UID
    val name: String = "",
    val address: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0,0.0),
    val description: String = "",
    val images: List<String> = listOf("")
)
