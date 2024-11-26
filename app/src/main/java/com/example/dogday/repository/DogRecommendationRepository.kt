package com.example.dogday.repository

import com.example.dogday.models.DogID
import com.example.dogday.models.DogRecommendation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DogRecommendationRepository(private val firestore: FirebaseFirestore) {

    suspend fun fetchDogRecommendations(): Map<DogID, DogRecommendation> {
        val dogRecommendations = mutableMapOf<DogID, DogRecommendation>()

        try {
            val snapshot = firestore.collection("dogRecommendations").get().await()
            for (document in snapshot.documents) {
                val id = document.getString("id") ?: continue
                val breed = document.getString("breed") ?: ""
                val description = document.getString("description") ?: ""
                val imageUrl = document.getString("imageUrl") ?: ""

                val dogID = DogID.valueOf(id.replace("-", "_").uppercase()
                )
                val dogRecommendation = DogRecommendation(breed, description, imageUrl)
                dogRecommendations[dogID] = dogRecommendation
            }
        } catch (e: Exception) {
            println("Error fetching dog recommendations: ${e.message}")
        }

        return dogRecommendations
    }

    // Fetch all dog recommendations as a list
    suspend fun fetchAllDogs(): List<DogRecommendation> {
        val dogList = mutableListOf<DogRecommendation>()
        try {
            val snapshot = firestore.collection("dogRecommendations").get().await()
            for (document in snapshot.documents) {
                val breed = document.getString("breed") ?: ""
                val description = document.getString("description") ?: ""
                val imageUrl = document.getString("imageUrl") ?: ""

                val dogRecommendation = DogRecommendation(breed, description, imageUrl)
                dogList.add(dogRecommendation)
            }
        } catch (e: Exception) {
            println("Error fetching dog list: ${e.message}")
        }
        return dogList
    }

    // Fetch a single dog recommendation by its Firestore document ID
    suspend fun fetchDogByBreed(breed: String): DogRecommendation? {
        return try {
            val snapshot = firestore.collection("dogRecommendations")
                .whereEqualTo("breed", breed)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val document = snapshot.documents[0]
                val description = document.getString("description") ?: ""
                val imageUrl = document.getString("imageUrl") ?: ""
                DogRecommendation(breed, description, imageUrl)
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error fetching dog by breed $breed: ${e.message}")
            null
        }
    }
}
