package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest


class UserEditViewModel(
    private val userRepository: UserRepository,
    private val bureauRepository: BureauRepository
) : UpdateViewModel() {

    companion object {
        private const val TAG = "UserEditViewModel"
    }

    private val _bureaux = MutableLiveData<List<Bureau>?>()
    val bureaux: LiveData<List<Bureau>?> get() = _bureaux

    private val _name = MutableLiveData<String>()
    private val _userName = MutableLiveData<String>()
    private val _bureau = MutableLiveData<String>()

    val name: LiveData<String> get() = _name
    val userName: LiveData<String> get() = _userName
    val bureau: LiveData<String> get() = _bureau

    val validData = MediatorLiveData<Boolean>().apply {
        addSource(_name) { validateData() }
        addSource(_userName) { validateData() }
        addSource(_bureau) { validateData() }
    }

    private fun validateData() {
        val validName = !_name.value.isNullOrBlank()
        val validUserName = !_name.value.isNullOrBlank()
        val validBureau = !_bureau.value.isNullOrBlank()
        val allValid =
            validName && validBureau && validUserName

        validData.value = allValid

    }

    fun getBureaux() {
        handleApiRequest(bureauRepository.getBureaux(), _loading, _bureaux, TAG)
    }

    fun setName(value: String) {
        _name.value = value
    }
    fun setUserName(value: String) {
        _userName.value = value
    }

    fun setBureau(value: String) {
        _bureau.value = value
    }


    fun putUser(userId: String, user: User) {
        putResult(userRepository.putUser(userId, user), TAG)
    }
}

