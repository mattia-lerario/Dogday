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
                            val imageUrl = dogInfo?.get("imageUrl") as? String

                            if (dogId != null && name != null && breed != null) {
                                Dog(
                                    dogId = dogId,
                                    name = name,
                                    nickName = nickName ?: "",
                                    breed = breed,
                                    birthday = birthday ?: 0L,
                                    breeder = breeder ?: "",
                                    imageUrl = imageUrl
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

    fun fetchDog(dogId: String, onSuccess: (Dog?) -> Unit, onFailure: (Exception) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("ddcollection").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val dogsMap = document.get("dogs") as? Map<*, *>
                        val dogData = dogsMap?.values?.find {
                            val dogInfo = it as? Map<*, *>
                            dogInfo?.get("dogId") == dogId
                        } as? Map<*, *>

                        if (dogData != null) {
                            val name = dogData["name"] as? String
                            val breed = dogData["breed"] as? String
                            val nickName = dogData["nickName"] as? String
                            val birthday = dogData["birthday"] as? Long
                            val breeder = dogData["breeder"] as? String
                            val imageUrl = dogData["imageUrl"] as? String

                            val dog = Dog(
                                dogId = dogId,
                                name = name ?: "",
                                nickName = nickName ?: "",
                                breed = breed ?: "",
                                birthday = birthday ?: 0L,
                                breeder = breeder ?: "",
                                imageUrl = imageUrl
                            )
                            onSuccess(dog)
                        } else {
                            onSuccess(null)
                        }
                    } else {
                        onSuccess(null)
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

