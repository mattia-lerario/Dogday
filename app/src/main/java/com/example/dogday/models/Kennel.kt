package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint

data class Kennel(
    val id: String,  // Make sure this is non-nullable
    val name: String?,
    val address: String?,
    val coordinates: GeoPoint?,
    val contactInfo: String?,
    val businessStatus: String?,
    val openingHours: Boolean?,
    val rating: Double?,
    val userRatingsTotal: Int?,
    val photoReference: String?,
    val iconUrl: String?,
    val types: List<String>?
){
    // No-argument constructor for Firebase deserialization
    constructor() : this(
        id = "",
        name = "",
        address = "",
        coordinates = null,
        contactInfo = null,
        businessStatus = null,
        openingHours = null,
        rating = null,
        userRatingsTotal = null,
        photoReference = null,
        iconUrl = null,
        types = null
    )
}