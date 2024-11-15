package com.example.dogday.repository

import com.example.dogday.models.Breeder
import com.google.firebase.firestore.FirebaseFirestore

class BreederRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchBreeders(onSuccess: (List<Breeder>) -> Unit, onFailure: (Exception) -> Unit) {
        val breederCollection = firestore.collection("BreedersDB")
        breederCollection.get().addOnSuccessListener { result ->
            val breederList = result.documents.mapNotNull { document ->
                val breeder = document.toObject(Breeder::class.java)
                breeder?.copy(id = document.id)
            }
            onSuccess(breederList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    fun getBreederById(breederId: String, onSuccess: (Breeder?) -> Unit, onFailure: (Exception) -> Unit) {
        val breederDocument = firestore.collection("BreedersDB").document(breederId)
        breederDocument.get().addOnSuccessListener { documentSnapshot ->
            val breeder = documentSnapshot.toObject(Breeder::class.java)?.copy(id = documentSnapshot.id)
            onSuccess(breeder)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}

