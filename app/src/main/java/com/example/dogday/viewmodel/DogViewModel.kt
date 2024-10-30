import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogday.models.Dog
import com.example.dogday.models.VetNote
import com.example.dogday.repository.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    private val dogRepository = DogRepository()

    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogList: StateFlow<List<Dog>> = _dogs.asStateFlow()

    private val _dog = MutableStateFlow<Dog?>(null)
    val dog: StateFlow<Dog?> = _dog.asStateFlow()

    init {
        fetchDogs()
    }

    fun fetchDogs() {
        dogRepository.fetchDogs(
            onSuccess = { dogs ->
                _dogs.value = dogs
            },
            onFailure = { exception ->
                println("Error fetching dogs: ${exception.message}")
            }
        )
    }

    fun fetchDog(dogId: String) {
        dogRepository.fetchDog(
            dogId = dogId,
            onSuccess = { fetchedDog ->
                _dog.value = fetchedDog
            },
            onFailure = { exception ->
                println("Error fetching dog: ${exception.message}")
                _dog.value = null
            }
        )
    }

    fun addNoteToDog(dog: Dog, note: VetNote, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updatedDog = dog.copy(vetLog = dog.vetLog + note)
        dogRepository.updateDog(updatedDog, onSuccess, onFailure)
    }

    fun updateVetNoteForDog(dog: Dog, updatedNote: VetNote) {
        val updatedVetLog = dog.vetLog.map { if (it.id == updatedNote.id) updatedNote else it }
        val updatedDog = dog.copy(vetLog = updatedVetLog)
        updateDog(updatedDog)
    }


    fun deleteVetNoteForDog(dog: Dog, noteToDelete: VetNote) {
        val updatedVetLog = dog.vetLog.filterNot { it == noteToDelete }
        val updatedDog = dog.copy(vetLog = updatedVetLog)
        updateDog(updatedDog)
    }

    private fun updateDog(dog: Dog) {
        viewModelScope.launch {
            dogRepository.updateDog(
                dog,
                onSuccess = {
                    _dog.value = dog
                    fetchDogs()
                },
                onFailure = { exception ->
                    println("Error updating dog: ${exception.message}")
                }
            )
        }
    }
}
