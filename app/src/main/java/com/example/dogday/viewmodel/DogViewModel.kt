import androidx.lifecycle.ViewModel
import com.example.dogday.models.Dog
import com.example.dogday.repository.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DogListViewModel : ViewModel() {

    private val dogRepository = DogRepository()

    // Opprett en MutableStateFlow med en tom liste
    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogList: StateFlow<List<Dog>> = _dogs.asStateFlow() // Eksponer som StateFlow

    init {
        fetchDogs()
    }

    private fun fetchDogs() {
        dogRepository.fetchDogs(
            onSuccess = { dogs ->
                _dogs.value = dogs // Oppdater verdien av _dogs direkte
            },
            onFailure = { exception ->
                println("Error fetching dogs: ${exception.message}")
            }
        )
    }
}

