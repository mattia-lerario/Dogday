package com.example.dogday.repository

import android.util.Log
import com.example.dogday.models.Kennel
import com.google.firebase.firestore.FirebaseFirestore

class KennelRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchKennels(onSuccess: (List<Kennel>) -> Unit, onFailure: (Exception) -> Unit) {
        val kennelCollection = firestore.collection("kennelsDB")
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
        val kennelDocument = firestore.collection("kennelsDB").document(kennelId)
        kennelDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val kennel = documentSnapshot.toObject(Kennel::class.java)?.copy(id = documentSnapshot.id)
                Log.d("KennelRepository", "Successfully fetched kennel: $kennel")
                onSuccess(kennel)
            } else {
                Log.e("KennelRepository", "No kennel found with ID: $kennelId")
                onSuccess(null)
            }
        }.addOnFailureListener { exception ->
            Log.e("KennelRepository", "Error fetching kennel: ${exception.message}")
            onFailure(exception)
        }
    }
}
