package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest
import retrofit2.Call

open class UpdateViewModel() : ViewModel() {
    val _loading = MutableLiveData<Boolean>()
    private val _updated = MutableLiveData<String?>()

    val loading: LiveData<Boolean> get() = _loading
    val updated: LiveData<String?> get() = _updated

    fun putResult(call: Call<String>, tag: String) {
        handleApiRequest(call, _loading, _updated, tag)
    }
}