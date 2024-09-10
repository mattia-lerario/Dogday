package com.example.myapptest01

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*

fun startPhoneLogin(activity: Activity, onComplete: (Boolean) -> Unit) {
    val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build()
    )

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    activity.startActivityForResult(signInIntent, 123) // Arbitrary request code
}

fun handlePhoneLoginResult(requestCode: Int, resultCode: Int, data: Intent?, onComplete: (Boolean) -> Unit) {
    if (requestCode == 123) {
        val response = IdpResponse.fromResultIntent(data)
        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            onComplete(true)
        } else {
            // Sign in failed
            onComplete(false)
        }
    }
}
