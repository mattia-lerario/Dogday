package com.example.dogday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.models.DogRecommendation
import com.example.dogday.models.DogID
import com.example.dogday.repository.DogRecommendationRepository
import com.example.dogday.repository.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogQuizViewModel(private val repository: DogRecommendationRepository) : ViewModel() {

    private val _dogRecommendations = MutableStateFlow<Map<DogID, DogRecommendation>>(emptyMap())
    val dogRecommendations: StateFlow<Map<DogID, DogRecommendation>> = _dogRecommendations

    init {
        fetchDogRecommendations()
    }

    private fun fetchDogRecommendations() {
        viewModelScope.launch {
            _dogRecommendations.value = repository.fetchDogRecommendations()
        }
    }
}
