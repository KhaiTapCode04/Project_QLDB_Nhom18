package database
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import database.data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

data class ApiResponse(
    val isSuccess: Boolean,
    val reason: String,
    val data: data,
)


data class data(
    val id: String = "",
    val username: String ="",
    val email: String = "",
)
interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    fun getTasksWithForm(@Field("username") email: String, @Field("password") password: String): Call<ApiResponse>
}
class login : ComponentActivity() {
    private val _userDataList = mutableStateOf<List<data>>(emptyList())
    val userDataList: State<List<data>> = _userDataList
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    val username = "hiêuqw1"
    val password = "1"

        login(username, password) { userData ->
            if (userData != null) {
                _userDataList.value = listOf(userData)
            } else {
                // Xử lý lỗi
                _userDataList.value = emptyList()
            }
        }
        setContent {
            displayTasks(userDataList.value)
        }
    }
    fun login(email: String, password: String, callback: (data?) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nettruyen.world/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        apiService.getTasksWithForm(email, password).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.isSuccess == true) {
                        callback(apiResponse.data)
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

}
@Composable
fun displayTasks(tasks: List<data>) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            tasks.forEach { task ->
                Get_task(task)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }}
@Composable
fun Get_task(task : data){
    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Text(
            text = task.id,
        )
        Text(
            text = task.username,
        )
        Text(
            text = task.email,
        )
    }
}