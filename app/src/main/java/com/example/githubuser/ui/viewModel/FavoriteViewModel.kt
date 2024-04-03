import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

        fun getAllUserFavorite() = repository.getListFavoriteUser()

        fun getFavoriteUserByUsername(username: String) =
            repository.getFavoriteUserByUsername(username)

        fun deleteFavoriteUser(userFavorite: FavoriteUser) {
            viewModelScope.launch {
                repository.deleteFavoriteUser(userFavorite)
            }
        }

        fun insertFavoriteUser(userFavorite: FavoriteUser) {
            viewModelScope.launch {
                repository.insertFavoriteUser(userFavorite)
            }
        }

    }
