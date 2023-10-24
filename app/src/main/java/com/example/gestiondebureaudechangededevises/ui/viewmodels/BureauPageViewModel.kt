package com.example.gestiondebureaudechangededevises.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.Transaction
import com.example.gestiondebureaudechangededevises.data.models.TransactionSchema
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.data.repositories.TransactionRepository
import com.example.gestiondebureaudechangededevises.utils.handleApiRequest

class BureauPageViewModel(
    private val bureauRepository: BureauRepository,
    private val transactionRepository: TransactionRepository
) : ListShowModel<TransactionSchema>() {

    companion object {
        private const val TAG = "BureauPageViewModel"
    }

    private val _bureau = MutableLiveData<Bureau?>()

    val bureau: LiveData<Bureau?> get() = _bureau

    fun getBureauById(bureauId: String) {
        handleApiRequest(bureauRepository.getBureauById(bureauId), super._loading, _bureau, TAG)
    }

    fun getTransactions(bureauId: String) {
        getList(transactionRepository.getBureauxTransactions(bureauId), TAG)
    }
}