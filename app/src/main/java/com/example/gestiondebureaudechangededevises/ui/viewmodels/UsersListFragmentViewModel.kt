package com.example.gestiondebureaudechangededevises.ui.viewmodels

import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository

class UsersListFragmentViewModel(private val repository: UserRepository) : ListShowModel<User>() {
    companion object {
        private const val TAG = "UsersListFragmentViewModel"
    }

    fun getUsers() {
        getList(repository.getUsers(), TAG)
    }

    fun deleteUser(userId: String) {
        deleteElement(repository.deleteUser(userId), TAG)
    }
}