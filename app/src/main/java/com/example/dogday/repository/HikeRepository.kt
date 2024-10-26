package com.example.dogday.repository

import com.example.dogday.models.HikeData
import com.google.firebase.firestore.FirebaseFirestore

class HikeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchHikeLocations(onSuccess: (List<HikeData>) -> Unit, onFailure: (Exception) -> Unit) {
        val hikeCollection = firestore.collection("hikeDB")
        hikeCollection.get().addOnSuccessListener { result ->
            val hikeList = result.documents.mapNotNull { document ->
                val hike = document.toObject(HikeData::class.java)
                hike?.copy(id = document.id)
            }
            onSuccess(hikeList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    fun getHikeById(hikeId: String, onSuccess: (HikeData?) -> Unit, onFailure: (Exception) -> Unit) {
        val hikeDocument = firestore.collection("hikeDB").document(hikeId)
        hikeDocument.get().addOnSuccessListener { documentSnapshot ->
            val hike = documentSnapshot.toObject(HikeData::class.java)
            onSuccess(hike)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}
