package com.example.dogday.repository

import com.example.dogday.models.Kennel
import com.google.firebase.firestore.FirebaseFirestore

class KennelRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchKennels(onSuccess: (List<Kennel>) -> Unit, onFailure: (Exception) -> Unit) {
        val kennelCollection = firestore.collection("kennelDB")
        kennelCollection.get().addOnSuccessListener { result ->
            val kennelList = result.documents.mapNotNull { document ->
                val kennel = document.toObject(Kennel::class.java)
                kennel?.copy(id = document.id)
            }
            onSuccess(kennelList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    fun getKennelById(kennelId: String, onSuccess: (Kennel?) -> Unit, onFailure: (Exception) -> Unit) {
        val kennelDocument = firestore.collection("kennelDB").document(kennelId)
        kennelDocument.get().addOnSuccessListener { documentSnapshot ->
            val kennel = documentSnapshot.toObject(Kennel::class.java)?.copy(id = documentSnapshot.id)
            onSuccess(kennel)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}
