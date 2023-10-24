package com.example.gestiondebureaudechangededevises.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.currencyCodes
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest

class AddBureauActivityViewModel(private val repository: BureauRepository) : ViewModel() {

    companion object {
        private const val TAG = "AddBureauActivityViewModel"
    }

    private val _loading = MutableLiveData<Boolean>()
    private val _added = MutableLiveData<Bureau?>()

    val loading: LiveData<Boolean> get() = _loading
    val added: LiveData<Bureau?> get() = _added

    //data entered
    private val _name = MutableLiveData<String>()
    var currencies = List(currencyCodes.size) {
        MutableLiveData("")
    }

    val name: LiveData<String> get() = _name

    val validData = MediatorLiveData<Boolean>(false).apply {
        addSource(_name) { validateTheData() }
        for (liveData in currencies) {
            addSource(liveData) { validateTheData() }
        }
    }

    fun setName(name: String) {
        _name.postValue(name)
    }


    private fun validateTheData() {
        val validName = !_name.value.isNullOrBlank()
//        var validCurrencies = true
//        for (currency in currencies) {
//            if (currency.value.isNullOrBlank())
//                validCurrencies = false
//        }
        validData.value = validName
    }

    fun addBureau(bureau: Bureau) {
        handleApiRequest(repository.addBureau(bureau), _loading, _added, TAG)
    }

    fun getStock(): Map<String, String> {
        val values = currencies.map { liveData ->
            if (!liveData.value.isNullOrBlank())
                liveData.value!!
            else "0"
        }
        Log.d(TAG, "values: $values")
        return currencyCodes.zip(values).toMap()
    }
}