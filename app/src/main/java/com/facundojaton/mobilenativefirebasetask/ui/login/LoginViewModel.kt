package com.facundojaton.mobilenativefirebasetask.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facundojaton.mobilenativefirebasetask.controllers.SessionController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    enum class LoginResult {
        SUCCESS, FAILED, WAITING, DONE_NAVIGATING
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _userEmail = MutableLiveData<String>()
    private val userEmail: LiveData<String>
        get() = _userEmail

    private val _userPassword = MutableLiveData<String>()
    private val userPassword: LiveData<String>
        get() = _userPassword

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    private var _seePassword = MutableLiveData<Boolean>()
    val seePassword: LiveData<Boolean>
        get() = _seePassword

    init {
        _seePassword.value = false
        checkSession()
    }

    private fun checkSession() {
        if (SessionController.authenticationState == SessionController.AuthenticationState.AUTHENTICATED) {
            _loginResult.value = LoginResult.SUCCESS
        } else SessionController.logout()
    }


    fun login() {
        uiScope.launch {
            _loginResult.value = LoginResult.WAITING
            val result = signInWithEmailAndPassword(userEmail.value, userPassword.value)
            if (result != null) {
                SessionController.initializeSignInData()
                _loginResult.value = LoginResult.SUCCESS
            } else {
                _loginResult.value = LoginResult.FAILED
                showError()
            }

        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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

    fun changeEmailContent(emailContent: String) {
        _userEmail.value = emailContent
    }

    fun changePasswordContent(password: String) {
        _userPassword.value = password
    }

    private fun showError() {
        _showSnackBarEvent.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }

    fun doneNavigatingToList() {
        _loginResult.value = LoginResult.DONE_NAVIGATING
    }

    fun togglePasswordVisibility() {
        _seePassword.value?.let {
            _seePassword.value = !it
        }
    }
}