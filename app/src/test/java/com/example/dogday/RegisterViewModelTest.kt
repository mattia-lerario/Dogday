package com.example.dogday.viewmodel


import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    private val mockAuth: FirebaseAuth = mock()

    @Before
    fun setup() {
        viewModel = RegisterViewModel()
    }

    @Test
    fun `onEmailChange updates email state`() = runTest {
        val testEmail = "test@example.com"
        viewModel.onEmailChange(testEmail)

        assertEquals(testEmail, viewModel.email.first())
    }

    @Test
    fun `onPasswordChange updates password state`() = runTest {
        val testPassword = "password123"
        viewModel.onPasswordChange(testPassword)

        assertEquals(testPassword, viewModel.password.first())
    }

    @Test
    fun `registerUser shows error for empty email`() = runTest {
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("password123")
        viewModel.registerUser()

        assertEquals("Email and password must not be empty", viewModel.registerError.first())
    }

    @Test
    fun `registerUser shows error for invalid email format`() = runTest {
        viewModel.onEmailChange("invalid-email")
        viewModel.onPasswordChange("password123")
        viewModel.registerUser()

        assertEquals("Invalid email format", viewModel.registerError.first())
    }

    @Test
    fun `registerUser shows error for password containing spaces`() = runTest {
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("pass word")
        viewModel.registerUser()

        assertEquals("Password cannot contain spaces or tabs", viewModel.registerError.first())
    }



}