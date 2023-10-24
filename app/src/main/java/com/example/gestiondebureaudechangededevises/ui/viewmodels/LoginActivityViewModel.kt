package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.Admin
import com.example.gestiondebureaudechangededevises.data.models.AdminCredentials
import com.example.gestiondebureaudechangededevises.data.repositories.AuthRepository
import com.example.gestiondebureaudechangededevises.utils.AdditionalCode
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest
import retrofit2.Response

class LoginActivityViewModel(private val repository: AuthRepository) : ViewModel() {

    companion object {
        private const val TAG = "LoginActivityViewModel"
    }

    private val _userName = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _helperMessage = MutableLiveData<String>()
    val isValidData = MediatorLiveData<Boolean>().apply {
        addSource(_userName) { validateData() }
        addSource(_password) { validateData() }
    }
    private val _admin = MutableLiveData<Admin?>()
    private val _isLoading = MutableLiveData<Boolean>()

    val userName: LiveData<String> get() = _userName
    val password: LiveData<String> get() = _password
    val helperMessage: LiveData<String> get() = _helperMessage
    val loading: LiveData<Boolean> get() = _isLoading
    val admin: LiveData<Admin?> get() = _admin

    private fun validateData() {
        val validUserName = !_userName.value.isNullOrBlank()
        val validPassword = !_password.value.isNullOrBlank()
        isValidData.value = validUserName && validPassword
    }

    fun setUserName(userName: String) {
        _userName.postValue(userName)
    }

    fun setPassword(password: String) {
        _password.postValue(password)
    }

    fun login(userCredentials: AdminCredentials) {
        handleApiRequest(
            repository.login(userCredentials),
            _isLoading,
            _admin,
            TAG,
            errorMessage = _helperMessage
        )
//        _technician.postValue(Technician.empty)
    }

}