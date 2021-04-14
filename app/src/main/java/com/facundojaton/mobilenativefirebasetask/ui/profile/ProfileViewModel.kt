package com.facundojaton.mobilenativefirebasetask.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facundojaton.mobilenativefirebasetask.controllers.SessionController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
            try {
                SessionController.logout()
                _logOutResult.value = LogOutResult.SUCCESS
            } catch (e: Exception) {
                _logOutResult.value = LogOutResult.FAILED
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}