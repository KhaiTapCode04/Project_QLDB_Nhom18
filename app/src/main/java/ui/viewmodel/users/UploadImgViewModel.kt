package ui.viewmodel.users
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.material3.Text
import data.model.UploadResponse
import data.model.User
import data.repository.UploadImg

object UploadImgViewModel {
    suspend fun UploadImg1(context: Context,user_id: Int,img: Bitmap): UploadResponse? {
        val check = UploadImg().uploadProfileImage(context,user_id,img)
        if(check!=null){
            val userPrefs = UserPreferencesManager(context)
            userPrefs.saveProfilePicture(check.imageUrl )
            return check
        }
        else{
            return null
        }

    }
}