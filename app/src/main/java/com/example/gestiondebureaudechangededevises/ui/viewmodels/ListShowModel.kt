package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.MessageResponse
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest
import retrofit2.Call

open class ListShowModel<T> : ViewModel() {

    private val _list = MutableLiveData<List<T>?>()
    val _loading = MutableLiveData<Boolean>()
    private val _deleted = MutableLiveData<String?>()
    private val _message = MutableLiveData<String>()

    val list: LiveData<List<T>?> get() = _list
    val loading: LiveData<Boolean> get() = _loading
    val deleted: LiveData<String?> get() = _deleted
    val message: LiveData<String> get() = _message

    fun getList(call: Call<List<T>>, tag: String) {
        handleApiRequest(call, _loading, _list, tag)
    }

    fun deleteElement(call: Call<String>, tag: String){
        handleApiRequest(call, _loading, _deleted, tag)
    }

    fun setMessage(message: String) {
        _message.postValue(message)
    }
}