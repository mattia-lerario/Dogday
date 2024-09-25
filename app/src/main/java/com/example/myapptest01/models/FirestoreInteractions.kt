package com.example.dogday

import android.util.Log
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
}
