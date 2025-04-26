package ui.viewmodel.users
import android.content.Context
import android.graphics.Bitmap
import data.model.User
import data.repository.UploadImg

object UploadImgViewModel {
    suspend fun UploadImg1(context: Context,user_id: Int,img: Bitmap): Boolean {
        val check = UploadImg().uploadProfileImage(context,user_id,img)
        return check?.isSuccess ?: false
    }
}