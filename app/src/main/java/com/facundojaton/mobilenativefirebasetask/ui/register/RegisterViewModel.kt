package com.facundojaton.mobilenativefirebasetask.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facundojaton.mobilenativefirebasetask.controllers.SessionController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {
    enum class RegisterResult {
        SUCCESS, FAILED, WAITING, DONE_NAVIGATING
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String>
        get() = _userEmail

    private var _userPassword = MutableLiveData<String>()
    val userPassword: LiveData<String>
        get() = _userPassword

    private var _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult>
        get() = _registerResult

    init {

    }

    fun signUp() {
        uiScope.launch {
            _registerResult.value = RegisterResult.WAITING
            val result = signUpWithEmailAndPassword(userEmail.value, userPassword.value)
            if (result != null) {
                Log.d("ACTIVITY", result.user.email)
                SessionController.initializeSignInData()
                _registerResult.value = RegisterResult.SUCCESS
            } else {
                _registerResult.value = RegisterResult.FAILED
                Log.e("ACTIVITY", "ERROR")
            }
        }
    }

    private suspend fun signUpWithEmailAndPassword(
        email: String?,
        password: String?
    ): AuthResult? {
        return withContext(Dispatchers.IO) {
            try {
                val data = FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .await()
                data
            } catch (e: Exception) {
                null
            }
        }
    }

    fun changeEmailContent(email: String) {
        _userEmail.value = email
    }

    fun changePasswordContent(password: String) {
        _userPassword.value = password
    }

    fun doneNavigatingToWelcomeScreen() {
        _registerResult.value = RegisterResult.DONE_NAVIGATING
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}