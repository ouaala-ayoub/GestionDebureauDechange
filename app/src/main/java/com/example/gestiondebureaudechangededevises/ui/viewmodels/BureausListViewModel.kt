package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository

class BureausListViewModel(private val repository: BureauRepository) : ListShowModel<Bureau>() {
    companion object {
        private const val TAG = "BureausListViewModel"
    }

    fun getBureaux() {
        getList(repository.getBureaux(), TAG)
    }

    fun deleteBureau(id: String) {
        deleteElement(repository.deleteBureau(id), TAG)
    }
}