package id.ac.ui.cs.mobileprogramming.fijar.ngebola.ui.start

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.db.LeagueRepository
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.db.UserRepository
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.db.league.League
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.db.user.User
import id.ac.ui.cs.mobileprogramming.fijar.ngebola.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class OnBoardingSharedViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var user: User
    private lateinit var league: League
    private val userRepository = UserRepository(application)
    private val leagueRepository = LeagueRepository(application)
    private val retrofit = RetrofitClient.RETROFIT_SERVICE
    val isDoneLoading = MutableLiveData<Boolean>()

    fun insertUserInfo(name: String, league_id: Int, image: Bitmap) {
        isDoneLoading.value = false
        GlobalScope.launch(Dispatchers.Main) {
            val jobs = withContext(Dispatchers.IO) {
                val imageString = convertBitmap(image)
                user = User(name = name, leagueId = league_id, image = imageString)
                userRepository.insertUser(user)

                val leagueResponse = retrofit.getLeagueInfoAsync(league_id).body()?.api?.leagues?.get(0)
                league = League(league_id = leagueResponse?.leagueId,
                        name = leagueResponse?.name,
                        country = leagueResponse?.country,
                        season = leagueResponse?.season)
                leagueRepository.insertLeague(league)
            }
            isDoneLoading.value = true
        }
    }

    private fun convertBitmap(bitmap: Bitmap): String {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        return Base64.getEncoder().encodeToString(bos.toByteArray())
    }
}