package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestiondebureaudechangededevises.data.models.Admin
import com.example.gestiondebureaudechangededevises.data.repositories.AuthRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest

class MainActivityViewModel(private val repository: AuthRepository) {

    companion object {
        private const val TAG = "MainActivityModel"
    }

    private val _admin = MutableLiveData<Admin?>()
    private val _isLoading = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean> get() = _isLoading
    val admin: LiveData<Admin?> get() = _admin

    fun getAuth() {
//        val admin = Admin(
//            id = "6519aeaa120996e9cacec732",
//            name = "testAdmin",
//            userName = "test-admin",
//            password = "testadmin123321",
//        )
        handleApiRequest(repository.getAuth(), _isLoading, _admin, TAG)
//        _admin.postValue(admin)
    }
}