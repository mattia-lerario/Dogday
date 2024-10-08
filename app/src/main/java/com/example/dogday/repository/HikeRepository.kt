package com.example.dogday.repository

import com.example.dogday.models.HikeData
import com.google.firebase.firestore.FirebaseFirestore

class HikeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchHikeLocations(onSuccess: (List<HikeData>) -> Unit, onFailure: (Exception) -> Unit) {
        val HikeLocationDataCollection = firestore.collection("hikeDB")
        HikeLocationDataCollection.get().addOnSuccessListener { result ->
            val HikeLocationList = result.documents.mapNotNull { document ->
                document.toObject(HikeData::class.java)
            }
            onSuccess(HikeLocationList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}
