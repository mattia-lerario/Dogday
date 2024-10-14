package com.example.dogday

import android.util.Log
import com.example.dogday.models.Dog
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreInteractions {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addUser(user: User) {
        db.collection("ddcollection")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "User data added successfully with UID: $user.uid.")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding user with UID: $user.uid", e)
            }
    }

    fun addDogToUser(uid: String, dog: Dog) {
        // Reference to the user's document
        db.collection("ddcollection").document(uid).update(
            "dogs.${dog.dogId}", dog
        ).addOnSuccessListener {
            Log.d("Firestore", "Dog added successfully.")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding dog", e)
        }
    }

}
