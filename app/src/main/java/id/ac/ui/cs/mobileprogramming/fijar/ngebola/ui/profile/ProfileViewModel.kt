package id.ac.ui.cs.mobileprogramming.fijar.ngebola.ui.profile

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.db.UserRepository
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.db.user.User
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    val languageName = MutableLiveData<String>()
    val userScreen = MutableLiveData<User>()
    val users = MutableLiveData<List<User>>()
    private var repository: UserRepository = UserRepository(application)


    fun getUser() {
        viewModelScope.launch {
            userScreen.value = repository.getUserByNameBg()
        }
    }


    fun getLanguage() {
        languageName.value = Locale.getDefault().displayName.toString()
    }

    fun convertToBitmap(stringImage: String): Bitmap {
        val decodedBytes = Base64.getDecoder().decode(stringImage)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}