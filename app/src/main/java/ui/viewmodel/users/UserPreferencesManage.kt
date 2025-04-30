package ui.viewmodel.users

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

class UserPreferencesManager(private val context: Context) {

    // Constants
    companion object {
        internal const val PREF_NAME = "UserPreferences"


        // Keys for user data
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_PROFILE_PICTURE = "profile_picture"
        private const val KEY_LOCAL_PROFILE_PICTURE = "local_profile_picture"

        // Default values
        private const val DEFAULT_USER_ID = 0
        private const val DEFAULT_USER_NAME = ""
        private const val DEFAULT_USER_EMAIL = ""
        private const val DEFAULT_USER_PHONE = ""
        private const val DEFAULT_PROFILE_PICTURE = ""
        private const val DEFAULT_LOCAL_PROFILE_PICTURE = ""
    }

    // Get the SharedPreferences instance
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Save user ID
    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    // Get user ID
    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, DEFAULT_USER_ID) ?: DEFAULT_USER_ID
    }

    // Save user name
    fun saveUserName(userName: String) {
        sharedPreferences.edit().putString(KEY_USER_NAME, userName).apply()
    }

    // Get user name
    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, DEFAULT_USER_NAME) ?: DEFAULT_USER_NAME
    }

    // Save user email
    fun saveUserEmail(userEmail: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, userEmail).apply()
    }

    // Get user email
    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, DEFAULT_USER_EMAIL) ?: DEFAULT_USER_EMAIL
    }
    fun saveUserPhone(userPhone: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, userPhone).apply()
    }
    fun getUserPhone(): String {
        return sharedPreferences.getString(KEY_USER_PHONE, DEFAULT_USER_PHONE) ?: DEFAULT_USER_PHONE
    }

    // Save profile picture URL or path
    fun saveProfilePicture(picturePath: String) {
        sharedPreferences.edit().putString(KEY_PROFILE_PICTURE, picturePath).apply()
    }

    // Get profile picture URL or path
    fun getProfilePicture(): String {
        return sharedPreferences.getString(KEY_PROFILE_PICTURE, DEFAULT_PROFILE_PICTURE)
            ?: DEFAULT_PROFILE_PICTURE
    }

    // Save local profile picture path
    private fun saveLocalProfilePicture(localPath: String) {
        sharedPreferences.edit().putString(KEY_LOCAL_PROFILE_PICTURE, localPath).apply()
    }

    // Get local profile picture path
    fun getLocalProfilePicture(): String {
        return sharedPreferences.getString(KEY_LOCAL_PROFILE_PICTURE, DEFAULT_LOCAL_PROFILE_PICTURE)
            ?: DEFAULT_LOCAL_PROFILE_PICTURE
    }

    // Save all user information at once
    fun saveUserInfo(userId: Int, userName: String, userEmail: String,userPhone: String, profilePicture: String?) {
        sharedPreferences.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            putString(KEY_USER_PHONE, userPhone)
            putString(KEY_PROFILE_PICTURE, profilePicture)
            apply()
        }
    }

    // Clear all user information (for logout)
    fun clearUserInfo() {
        // Delete local profile picture if it exists
        val localProfilePath = getLocalProfilePicture()
        if (localProfilePath.isNotEmpty()) {
            val file = File(localProfilePath)
            if (file.exists()) {
                file.delete()
            }
        }

        sharedPreferences.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_PHONE)
            remove(KEY_PROFILE_PICTURE)
            remove(KEY_LOCAL_PROFILE_PICTURE)
            apply()
        }
    }

    // Check if user is logged in
    fun isUserLoggedIn(): Int {
        return getUserId()
    }

    /**
     * Download and save profile image from URL
     * This function downloads an image from a URL and saves it to internal storage
     * It then saves the local path in SharedPreferences
     *
     * @param imageUrl URL of the image to download
     * @return Path to the saved image file, or null if download failed
     */
    suspend fun downloadAndSaveProfileImage(imageUrl: String): String? = withContext(Dispatchers.IO) {
        try {
            // Create a connection to the URL
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            // Check if the connection was successful
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext null
            }

            // Get the input stream and decode it to a bitmap
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            connection.disconnect()

            if (bitmap == null) {
                return@withContext null
            }

            // Create a file to save the image
            val fileName = "profile_${UUID.randomUUID()}.jpg"
            val directory = context.getDir("profile_images", Context.MODE_PRIVATE)
            val file = File(directory, fileName)

            // Delete old profile image if it exists
            val oldLocalPath = getLocalProfilePicture()
            if (oldLocalPath.isNotEmpty()) {
                val oldFile = File(oldLocalPath)
                if (oldFile.exists()) {
                    oldFile.delete()
                }
            }

            // Save the bitmap to the file
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            // Save the local path in SharedPreferences
            val localPath = file.absolutePath
            saveLocalProfilePicture(localPath)

            // Also save the original URL
            saveProfilePicture(imageUrl)

            return@withContext localPath
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Get the profile image as a Bitmap
     * This function tries to load the image from the local storage first
     *
     * @return Bitmap of the profile image, or null if not found
     */
    fun getProfileImageBitmap(): Bitmap? {
        val localPath = getLocalProfilePicture()
        if (localPath.isEmpty()) {
            return null
        }

        try {
            val file = File(localPath)
            if (!file.exists()) {
                return null
            }

            return BitmapFactory.decodeFile(localPath)
        } catch (e: Exception) {
            return null
        }
    }
}