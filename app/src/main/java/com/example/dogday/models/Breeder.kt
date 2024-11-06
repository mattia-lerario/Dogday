package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint

data class Breeder(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val ownerName: String = "",
    val contactInfo: String = "",
    val description: String = "",
    val dogBreeds: ArrayList<String> = arrayListOf(), // Changed to ArrayList to store multiple breeds
    val imageUrl: String = ""
)
