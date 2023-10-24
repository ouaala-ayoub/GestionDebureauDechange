package com.example.gestiondebureaudechangededevises.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest

class AddUserActivityViewModel(
    private val repository: UserRepository,
    private val bureauRepository: BureauRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AddUserActivityViewModel"
    }

    fun getBureaux() {
        handleApiRequest(bureauRepository.getBureaux(), _loading, _bureaux, TAG)
    }

    private val _bureaux = MutableLiveData<List<Bureau>?>()
    private val _added = MutableLiveData<User?>()
    private val _loading = MutableLiveData<Boolean>()
    private val _requestLoading = MutableLiveData<Boolean>()

    val added: LiveData<User?> get() = _added
    val loading: LiveData<Boolean> get() = _loading
    val requestLoading: LiveData<Boolean> get() = _requestLoading
    val bureaux: LiveData<List<Bureau>?> get() = _bureaux

    private val _name = MutableLiveData<String>()
    private val _userName = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _retypedPassword = MutableLiveData<String>()
    private val _bureau = MutableLiveData<String>()

    val validData = MediatorLiveData<Boolean>().apply {
        addSource(_name) { validateData() }
        addSource(_userName) { validateData() }
        addSource(_password) { validateData() }
        addSource(_retypedPassword) { validateData() }
        addSource(_bureau) { validateData() }
    }

    private fun validateData() {
        val validName = !_name.value.isNullOrBlank()
        val validUserName = !_userName.value.isNullOrBlank()
        val validPassword = !_password.value.isNullOrBlank()
        val validRetypedPassword = _retypedPassword.value.toString() == _password.value.toString()
        val validBureau = !_bureau.value.isNullOrBlank()
        val allValid =
            validName && validUserName && validPassword && validRetypedPassword && validBureau

        validData.value = allValid

    }

    val name: LiveData<String> get() = _name
    val userName: LiveData<String> get() = _userName
    val password: LiveData<String> get() = _password
    val bureau: LiveData<String> get() = _bureau

    fun setName(value: String) {
        _name.value = value
    }

    fun setUserName(value: String) {
        _userName.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun setRetypedPassword(value: String) {
        _retypedPassword.value = value
    }

    fun setBureau(value: String) {
        _bureau.value = value
    }

    fun addUser(user: User) {
        handleApiRequest(repository.addUser(user), _requestLoading, _added, TAG)
    }
}