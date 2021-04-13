package com.facundojaton.mobilenativefirebasetask.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email
}