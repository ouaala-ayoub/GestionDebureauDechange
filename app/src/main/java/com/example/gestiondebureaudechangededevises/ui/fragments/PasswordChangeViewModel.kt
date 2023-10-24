package com.example.gestiondebureaudechangededevises.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest

class PasswordChangeViewModel : ViewModel() {

    companion object {
        private const val TAG = "PasswordChangeViewModel"
    }

    private val repository = UserRepository.getInstance()
    private val _loading = MutableLiveData<Boolean>()
    private val _passwordChanged = MutableLiveData<String?>()

    val loading: LiveData<Boolean> get() = _loading
    val passwordChanged: LiveData<String?> get() = _passwordChanged

    // Simulate a password change
    fun changePassword(userId: String, newPassword: String) {
        handleApiRequest(
            repository.putPassword(userId, newPassword),
            _loading,
            _passwordChanged,
            TAG
        )
    }

    private fun performPasswordChange(newPassword: String) {

    }
}
