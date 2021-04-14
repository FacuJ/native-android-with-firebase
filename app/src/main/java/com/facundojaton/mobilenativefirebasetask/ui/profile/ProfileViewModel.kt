package com.facundojaton.mobilenativefirebasetask.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facundojaton.mobilenativefirebasetask.controllers.SessionController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class ProfileViewModel : ViewModel() {
    enum class LogOutResult {
        SUCCESS, FAILED, WAITING
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private var _logOutResult = MutableLiveData<LogOutResult>()
    val logOutResult: LiveData<LogOutResult>
        get() = _logOutResult

    init {
        _email.value = SessionController.userEmail
    }

    fun logOut() {
        uiScope.launch {
            _logOutResult.value = LogOutResult.WAITING
            val result = signOut()
            if (result) {
                SessionController.userEmail = null
                Log.d("ACTIVITY", "Logged out")
                _logOutResult.value = LogOutResult.SUCCESS
            } else {
                Log.e("ACTIVITY", "ERROR")
                _logOutResult.value = LogOutResult.FAILED
            }
        }
    }

    private suspend fun signOut(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                FirebaseAuth.getInstance().signOut()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}