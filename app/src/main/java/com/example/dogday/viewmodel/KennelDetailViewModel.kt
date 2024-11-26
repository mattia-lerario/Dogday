package com.example.dogday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.models.Kennel
import com.example.dogday.repository.KennelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KennelDetailViewModel : ViewModel() {

    private val kennelRepository = KennelRepository()

    private val _kennel = MutableStateFlow<Kennel?>(null)
    val kennel: StateFlow<Kennel?> = _kennel

    fun fetchKennelById(kennelId: String) {
        viewModelScope.launch {
            kennelRepository.getKennelById(
                kennelId,
                onSuccess = { fetchedKennel ->
                    _kennel.value = fetchedKennel
                },
                onFailure = { _ ->
                    // Handle error
                    _kennel.value = null
                }
            )
        }
    }

}
