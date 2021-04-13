package com.facundojaton.mobilenativefirebasetask.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    enum class LoginResult {
        SUCCESS, FAILED
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String>
        get() = _userEmail

    private val _userPassword = MutableLiveData<String>()
    val userPassword: LiveData<String>
        get() = _userPassword

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    fun login() {
        uiScope.launch {
            val result = signInWithEmailAndPassword(userEmail.value, userPassword.value)
            if (result != null) {
                _loginResult.value = LoginResult.SUCCESS
                Log.d("ACTIVITY", result.user.email)
            } else {
                Log.e("ACTIVITY", "ERROR")
                _loginResult.value = LoginResult.FAILED
            }

        }
    }

    private suspend fun signInWithEmailAndPassword(
        email: String?,
        password: String?
    ): AuthResult? {
        return withContext(Dispatchers.IO) {
            try {
                val data = FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .await()
                data
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun changeEmailContent(emailContent: String) {
        _userEmail.value = emailContent
    }

    fun changePasswordContent(password: String) {
        _userPassword.value = password
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}