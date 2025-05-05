package ui.viewmodel.contact
//
//import android.content.Context
//import android.util.Log
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.gson.annotations.SerializedName
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Field
//import retrofit2.http.FormUrlEncoded
//import retrofit2.http.POST
//import ui.viewmodel.users.UserPreferencesManager
//
//class AddContactViewModel : ViewModel() {
//    val name = mutableStateOf("")
//    val email = mutableStateOf("")
//    val phone = mutableStateOf("")
//    val addContactResult = mutableStateOf<String?>(null)
//
//
//
//
//    private fun getUserIdFromPrefs(context: Context): Int? {
//        val sharedPref = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
//        return sharedPref.getInt("user_id", -1)
//    }
//
//    fun addContact(context: Context) {
//
//        val currentName = name.value
////        val currentUserId = getUserIdFromPrefs(context).toString()
//        val currentUserId = UserPreferencesManager(context).getUserId().toString()
//        val currentEmail = email.value
//        val currentPhone = phone.value
//        Log.d(
//            "Add contact:",
//            "uid: $currentUserId, name: $currentName, email: $currentEmail, phone: $currentPhone."
//        )
//        if (currentUserId == "-1") {
//            addContactResult.value = "Không tìm thấy userId trong thiết bị"
//            return
//        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            Log.e("AddContact", "Bắt đầu launch")
//            try {
//                Log.e(
//                    "AddContact",
//                    "Gọi API addContact với name: $currentName - userId: $currentUserId"
//                )
//
//                val contactResponse =
//                    apiService.addContact(currentName, currentUserId, "1").execute()
//                Log.e(
//                    "AddContact",
//                    "Đã nhận response: isSuccessful = ${contactResponse.isSuccessful}"
//                )
//                if (contactResponse.isSuccessful) {
//
//                    val rawBodyText = contactResponse.errorBody()?.string()
//                    Log.e("AddContact", "Error body = $rawBodyText")
//
//                    val successBody = contactResponse.body()
//                    Log.e("AddContact", "Body = $successBody")
//
//                    val raw = contactResponse.raw()
//                    Log.e("AddContact", "Raw full: $raw")
//
//
//                    val rawResponseText = contactResponse.raw().toString()
//                    Log.e("AddContact", "Raw HTTP: $rawResponseText")
//
//                    val contactId = contactResponse.body()?.data?.get(0)?.contact_id
//                    Log.d("contactidvuatao: ", ": $contactId")
//                    if (contactId != null) {
//                        Log.e("AddContact", "Contact ID: $contactId")
//
//                        val emailResponse =
//                            apiService.addEmail(contactId.toInt(), "personal", currentEmail)
//                                .execute()
//
//
//                        if (emailResponse.isSuccessful) {
//                            Log.e("AddContact", "Thêm email thành công")
//
//                            val phoneResponse =
//                                apiService.addPhone(contactId.toInt(), "mobile", currentPhone)
//                                    .execute()
//                            if (phoneResponse.isSuccessful) {
//                                Log.e("AddContact", "Thêm phone thành công")
//                                addContactResult.value = "Thêm contact thành công!"
//                            } else {
//                                addContactResult.value = "Thêm phone thất bại"
//                            }
//                        } else {
//                            addContactResult.value = "Thêm email thất bại"
//                        }
//                    } else {
//                        addContactResult.value = "Không lấy được Contact ID"
//                    }
//                } else {
//                    addContactResult.value = "Thêm contact thất bại"
//                }
//            } catch (e: Exception) {
//                Log.e("AddContact", "Lỗi: ${e.localizedMessage}")
//                addContactResult.value = "Lỗi kết nối hoặc server"
//            }
//        }
//    }
//
//    fun clearAllfields(){
//        name.value = ""
//        email.value = ""
//        phone.value = ""
//    }
//
//
//}
//
//data class AddContactResponse(
//    val isSuccess: Boolean,
//    val data: List<ContactData>,
//    val reason: String
//) {
//    data class ContactData(
//        val contact_id: String,
//        val user_id: String,
//        val name: String,
//        val group_id: String
//    )
//}
//
//data class AddEmailResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("reason") val reason: String
//)
//
//data class AddPhoneResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("reason") val reason: String
//)