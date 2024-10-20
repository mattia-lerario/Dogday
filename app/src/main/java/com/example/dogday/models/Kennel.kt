package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint

data class Kennel(
    val name: String = "",
    val address: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val ownerName: String = "",
    val contactInfo: String = "",
    val description: String = "",
    val imageUrl: String = ""
)
