package ui.viewmodel.group

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ui.viewmodel.group.GroupViewModel

class GroupViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            return GroupViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}