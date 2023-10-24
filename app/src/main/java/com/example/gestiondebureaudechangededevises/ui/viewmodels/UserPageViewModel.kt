package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest

class UserPageViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        private const val TAG = "UserPageViewModel"
    }

    private val _loading = MutableLiveData<Boolean>()
    private val _user = MutableLiveData<User?>()

    val loading: LiveData<Boolean> get() = _loading
    val user: LiveData<User?> get() = _user

    fun getUserById(userId: String) {
        handleApiRequest(repository.getUserById(userId), _loading, _user, TAG)
    }

}