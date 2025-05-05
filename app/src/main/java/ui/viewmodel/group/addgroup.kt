import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Field
import ui.viewmodel.users.UserPreferencesManager

// API service
interface GroupApiService {
    @FormUrlEncoded
    @POST("group/addgroup.php")
    suspend fun addGroup(
        @Field("user_id") user_id: Int,
        @Field("name") name: String,
        @Field("description") description: String
    ): ApiResponse
}

data class ApiResponse(
    val status: String? = null,
    val message: String? = null
)

class AddGroupViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow<AddGroupState>(AddGroupState.Idle)
    val uiState: StateFlow<AddGroupState> = _uiState

    private val _event = Channel<AddGroupEvent>()
    val event = _event.receiveAsFlow()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GroupApiService::class.java)

    fun onNameChanged(newName: String) {
        name = newName
    }

    fun onDescriptionChanged(newDescription: String) {
        description = newDescription
    }

    fun addGroup(context: Context) {
        val userId = UserPreferencesManager(context).getUserId()

        Log.d("AddGroup", "Bắt đầu tạo group với dữ liệu:")
        Log.d("AddGroup", "User ID: $userId")
        Log.d("AddGroup", "Name: $name")
        Log.d("AddGroup", "Description: $description")

        viewModelScope.launch {
            _uiState.value = AddGroupState.Loading

            try {
                val response = api.addGroup(userId, name, description)

                Log.d("AddGroup", "API trả về thành công: ${response.message}")

                _uiState.value = AddGroupState.Success(response.message ?: "Tạo group thành công.")
                _event.send(AddGroupEvent.Success(response.message ?: "Tạo group thành công."))

            } catch (e: Exception) {
                Log.e("AddGroup", "Lỗi khi tạo group: ${e.message}", e)

                _uiState.value = AddGroupState.Error(e.message ?: "Đã xảy ra lỗi.")
                _event.send(AddGroupEvent.Error(e.message ?: "Đã xảy ra lỗi."))
            }
        }
    }
}

sealed class AddGroupState {
    object Idle : AddGroupState()
    object Loading : AddGroupState()
    data class Success(val message: String) : AddGroupState()
    data class Error(val error: String) : AddGroupState()
}

sealed class AddGroupEvent {
    data class Success(val message: String) : AddGroupEvent()
    data class Error(val message: String) : AddGroupEvent()
}
