package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint

data class Breeder(
    val id: String,  // Make sure this is non-nullable
    val name: String?,
    val address: String?,
    val coordinates: GeoPoint?,
    val contactInfo: String?,
    val dogBreeds: List<String>?,
    val businessStatus: String?,
    val openingHours: Boolean?,
    val rating: Double?,
    val userRatingsTotal: Int?,
    val photoReference: String?,
    val iconUrl: String?,
    val types: List<String>?
){
    // No-argument constructor required for Firebase
    constructor() : this("", "", "", null, null, emptyList(), null, null, null, null, null, null, null)
}