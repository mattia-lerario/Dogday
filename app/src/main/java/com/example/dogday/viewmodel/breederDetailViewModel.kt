package com.example.dogday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.models.Breeder
import com.example.dogday.repository.BreederRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BreederDetailViewModel : ViewModel() {

    private val breederRepository = BreederRepository()

    private val _breeder = MutableStateFlow<Breeder?>(null)
    val breeder: StateFlow<Breeder?> = _breeder

    fun fetchBreederById(breederId: String) {
        viewModelScope.launch {
            breederRepository.getBreederById(
                breederId,
                onSuccess = { fetchedBreeder ->
                    _breeder.value = fetchedBreeder
                },
                onFailure = {
                    _breeder.value = null
                }
            )
        }
    }
}
