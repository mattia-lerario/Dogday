// AddDogViewModel.kt

package com.example.dogday.viewmodel

import android.Manifest
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.FirestoreInteractions
import com.example.dogday.models.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddDogViewModel : ViewModel() {
    private val firestoreInteractions = FirestoreInteractions()
    private val storage = FirebaseStorage.getInstance()

    private val _dogName = MutableStateFlow("")
    val dogName: StateFlow<String> = _dogName

    private val _dogNickName = MutableStateFlow("")
    val dogNickName: StateFlow<String> = _dogNickName

    private val _dogBreed = MutableStateFlow("")
    val dogBreed: StateFlow<String> = _dogBreed

    private val _dogBreeder = MutableStateFlow("")
    val dogBreeder: StateFlow<String> = _dogBreeder

    private val _dogBirthday = MutableStateFlow(0L)
    val dogBirthday: StateFlow<Long> = _dogBirthday

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker

    private val _dogImageBitmap = MutableStateFlow<Bitmap?>(null)
    val dogImageBitmap: StateFlow<Bitmap?> = _dogImageBitmap

    private val _uploadingImage = MutableStateFlow(false)
    val uploadingImage: StateFlow<Boolean> = _uploadingImage

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    fun onDogNameChange(newName: String) {
        _dogName.value = newName
    }

    fun onDogNickNameChange(newNickName: String) {
        _dogNickName.value = newNickName
    }

    fun onDogBreedChange(newBreed: String) {
        _dogBreed.value = newBreed
    }

    fun onDogBreederChange(newBreeder: String) {
        _dogBreeder.value = newBreeder
    }

    fun onDogBirthdayChange(newBirthday: Long) {
        _dogBirthday.value = newBirthday
    }

    fun toggleDatePicker() {
        _showDatePicker.value = !_showDatePicker.value
    }

    fun onDogImageCaptured(bitmap: Bitmap?) {
        _dogImageBitmap.value = bitmap
    }

    fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        onResult(true) // Simuler tillatelse. I praksis håndteres dette i AddDogScreen
    }

    fun captureImage(launcher: (Unit?) -> Unit) {
        launcher(null) // Starter kamera
    }

    fun saveDogData() {
        if (_dogName.value.isNotEmpty() && _dogBreed.value.isNotEmpty() && _dogImageBitmap.value != null) {
            _uploadingImage.value = true

            val storageRef = storage.reference.child("dog_images/${_dogName.value}_${System.currentTimeMillis()}.jpg")
            val baos = ByteArrayOutputStream()
            _dogImageBitmap.value?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            storageRef.putBytes(data)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        val dog = Dog(
                            name = _dogName.value,
                            nickName = _dogNickName.value,
                            breed = _dogBreed.value,
                            birthday = _dogBirthday.value,
                            breeder = _dogBreeder.value,
                            imageUrl = uri.toString()
                        )

                        if (uid != null) {
                            firestoreInteractions.addDogToUser(uid, dog)
                            _saveSuccess.value = true
                        }
                    }
                }.addOnFailureListener {
                    _uploadingImage.value = false
                }
        }
    }
}
