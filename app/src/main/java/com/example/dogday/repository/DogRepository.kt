package com.example.dogday.repository

import com.example.dogday.models.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DogRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun fetchDogs(onSuccess: (List<Dog>) -> Unit, onFailure: (Exception) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("ddcollection").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val dogsMap = document.get("dogs") as? Map<*, *>
                        val dogs = dogsMap?.values?.mapNotNull { dogData ->
                            val dogInfo = dogData as? Map<*, *>
                            val dogId = dogInfo?.get("dogId") as? String
                            val name = dogInfo?.get("name") as? String
                            val breed = dogInfo?.get("breed") as? String
                            val nickName = dogInfo?.get("nickName") as? String
                            val birthday = dogInfo?.get("birthday") as? Long
                            val breeder = dogInfo?.get("breeder") as? String

                            if (dogId != null && name != null && breed != null) {
                                Dog(
                                    dogId = dogId,
                                    name = name,
                                    nickName = nickName ?: "",
                                    breed = breed,
                                    birthday = birthday ?: 0L,
                                    breeder = breeder ?: ""
                                )
                            } else {
                                null
                            }
                        } ?: emptyList()
                        onSuccess(dogs)
                    } else {
                        println("Finner ikke dokument.")
                        onSuccess(emptyList())
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Bruker ikke logget inn."))
        }
    }
}
