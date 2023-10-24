package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.currencyCodes
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository

class BureauEditViewModel(
    private val bureauRepository: BureauRepository,
    private val bureau: Bureau
) :
    UpdateViewModel() {
    companion object {
        private const val TAG = "BureauEditViewModel"
    }

    //data entered
    private val _name = MutableLiveData<String>()
    var currencies = List(bureau.stock!!.size) {
        MutableLiveData("")
    }

    val name: LiveData<String> get() = _name


    val validData = MediatorLiveData<Boolean>().apply {
        addSource(_name) { validateTheData() }
        for (liveData in currencies) {
            addSource(liveData) { validateTheData() }
        }
    }

    fun setName(name: String) {
        _name.postValue(name)
    }

    fun setCurrencyAt(position: Int, value: String) {
        currencies[position].postValue(value)
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

    fun putBureau(bureauId: String, bureau: Bureau) {
        putResult(bureauRepository.putBureau(bureauId, bureau), TAG)
    }

    fun getStock(): Map<String, String> {
        val values = currencies.map { liveData ->
            if (!liveData.value.isNullOrBlank())
                liveData.value!!
            else "0"
        }

        return currencyCodes.zip(values).toMap()
    }
}