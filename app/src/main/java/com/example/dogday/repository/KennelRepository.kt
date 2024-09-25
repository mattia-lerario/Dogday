package com.example.dogday.repository

import com.example.dogday.models.Kennel
import com.google.firebase.firestore.FirebaseFirestore

class KennelRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchKennels(onSuccess: (List<Kennel>) -> Unit, onFailure: (Exception) -> Unit) {
        val kennelsCollection = firestore.collection("kennelsDB")
        kennelsCollection.get().addOnSuccessListener { result ->
            val kennelsList = result.documents.mapNotNull { document ->
                document.toObject(Kennel::class.java)
            }
            onSuccess(kennelsList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}
