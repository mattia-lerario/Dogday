package com.example.dogday.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.models.HikeData
import com.example.dogday.repository.HikeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HikeDetailViewModel : ViewModel() {

    private val hikeRepository = HikeRepository()

    private val _hike = MutableStateFlow<HikeData?>(null)
    val hike: StateFlow<HikeData?> = _hike

    fun fetchHikeById(hikeId: String) {
        viewModelScope.launch {
            hikeRepository.getHikeById(
                hikeId,
                onSuccess = { fetchedHike ->
                    _hike.value = fetchedHike
                },
                onFailure = { _ ->
                    // Handle error, e.g., log it or show a message
                    _hike.value = null
                }
            )
        }
    }
}

