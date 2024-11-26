package com.example.dogday.repository

import android.util.Log
import com.example.dogday.models.Breeder
import com.google.firebase.firestore.FirebaseFirestore

class BreederRepository {
    private val firestore = FirebaseFirestore.getInstance()
    // Fetch breeders from DB.
    fun fetchBreeders(onSuccess: (List<Breeder>) -> Unit, onFailure: (Exception) -> Unit) {
        val breederCollection = firestore.collection("breeders")
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
    //get specific Breeder by ID
    fun getBreederById(breederId: String, onSuccess: (Breeder?) -> Unit, onFailure: (Exception) -> Unit) {
        val breederDocument = firestore.collection("breeders").document(breederId)
        breederDocument.get().addOnSuccessListener { documentSnapshot ->
            val breeder = documentSnapshot.toObject(Breeder::class.java)?.copy(id = documentSnapshot.id)
            onSuccess(breeder)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
    //not in use as we are using the Search Places function instead
    fun uploadBreedersToFirestore(breeders: List<Breeder>) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("breeders")

        breeders.forEach { breeder ->
            val documentRef = collectionRef.document()  // Generate Firestore ID here
            val breederWithId = breeder.copy(id = documentRef.id) // Update the breeder object with Firestore-generated ID
            documentRef.set(breederWithId)
                .addOnSuccessListener {
                    Log.d("Firestore", "Successfully uploaded breeder: ${breederWithId.name}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error uploading breeder: ${breederWithId.name}", e)
                }
        }
    }
}

