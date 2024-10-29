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

                // Convert Firestore ID string to DogID enum
                val dogID = DogID.valueOf(id.replace("-", "_").toUpperCase())
                val dogRecommendation = DogRecommendation(breed, description, imageUrl)
                dogRecommendations[dogID] = dogRecommendation
            }
        } catch (e: Exception) {
            println("Error fetching dog recommendations: ${e.message}")
        }

        return dogRecommendations
    }
}
