package com.example.dogday.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dogday.FirestoreInteractions
import com.example.dogday.models.Dog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class AddDogViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddDogViewModel
    private lateinit var fakeFirestoreInteractions: FirestoreInteractions
    private lateinit var fakeFirebaseAuth: FirebaseAuth
    private lateinit var fakeFirebaseStorage: FirebaseStorage
    private lateinit var fakeStorageReference: StorageReference

    @Before
    fun setUp() {
        val fakeFirebaseUser = mock<FirebaseUser> {
            on { uid } doReturn "mockUserId"
        }
        fakeFirebaseAuth = mock {
            on { currentUser } doReturn fakeFirebaseUser
        }

        val fakeTaskUri: Task<Uri> = mock {
            on { addOnSuccessListener(any()) } doAnswer { invocation ->
                val listener = invocation.getArgument<OnSuccessListener<Uri>>(0)
                val mockUri: Uri = mock { on { toString() } doReturn "mockUrl" }
                listener.onSuccess(mockUri)
                mock()
            }
        }

        val fakeUploadTask: UploadTask = mock {
            on { addOnSuccessListener(any()) } doAnswer { invocation ->
                val listener = invocation.getArgument<OnSuccessListener<UploadTask.TaskSnapshot>>(0)
                listener.onSuccess(mock())
                this.mock
            }
        }

        fakeStorageReference = mock {
            on { child(any()) } doReturn this.mock
            on { putBytes(any()) } doReturn fakeUploadTask
            on { downloadUrl } doReturn fakeTaskUri
        }

        fakeFirebaseStorage = mock {
            on { reference } doReturn fakeStorageReference
        }

        fakeFirestoreInteractions = mock()

        viewModel = AddDogViewModel(
            firestoreInteractions = fakeFirestoreInteractions,
            firebaseStorage = fakeFirebaseStorage,
            firebaseAuth = fakeFirebaseAuth
        )
    }


    @Test
    fun `onDogNameChange updates dogName state`() = runTest {
        val newName = "Buddy"
        viewModel.onDogNameChange(newName)
        assertEquals(newName, viewModel.dogName.first())
    }

    @Test
    fun `toggleDatePicker toggles showDatePicker state`() = runTest {
        val initialState = viewModel.showDatePicker.first()
        viewModel.toggleDatePicker()
        assertEquals(!initialState, viewModel.showDatePicker.first())
    }

    @Test
    fun `saveDogData uploads image and calls FirestoreInteractions`() = runTest {
        val testBitmap: Bitmap = mock()
        viewModel.onDogImageCaptured(testBitmap)
        viewModel.onDogNameChange("TestDog")
        viewModel.onDogBreedChange("TestBreed")

        viewModel.saveDogData()

        verify(fakeFirestoreInteractions).addDogToUser(eq("mockUserId"), check {
            assertEquals("TestDog", it.name)
            assertEquals("TestBreed", it.breed)
            assertEquals("mockUrl", it.imageUrl)
        })
    }
}
