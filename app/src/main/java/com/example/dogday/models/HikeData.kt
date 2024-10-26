package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint

data class HikeData(
    val id: String = "",
    val title: String = "",
    val address: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val description: String = "",
    val imageUrl: String = "",
    val images: List<String> = listOf("")
)

