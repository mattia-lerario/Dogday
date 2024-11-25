package com.example.dogday.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dogday.models.DogRecommendation
import com.example.dogday.models.DogID
import com.example.dogday.repository.DogRecommendationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class DogQuizViewModelTest {

    private lateinit var viewModel: DogQuizViewModel
    private lateinit var mockRepository: DogRecommendationRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mock()
        viewModel = DogQuizViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `fetchDogRecommendations updates state with repository data`() = testScope.runTest {
        // Arrange
        val expectedRecommendations = mapOf(
            DogID.GOLDEN_RETRIEVER to DogRecommendation(
                breed = "Golden Retriever",
                description = "Friendly and energetic",
                imageUrl = "https://example.com/golden.jpg"
            ),
            DogID.LABRADOR to DogRecommendation(
                breed = "Labrador",
                description = "Great with families",
                imageUrl = "https://example.com/labrador.jpg"
            )
        )

        whenever(mockRepository.fetchDogRecommendations()).thenReturn(expectedRecommendations)

        // Act
        viewModel = DogQuizViewModel(mockRepository) // This triggers fetch
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(expectedRecommendations, viewModel.dogRecommendations.value)
    }
}

