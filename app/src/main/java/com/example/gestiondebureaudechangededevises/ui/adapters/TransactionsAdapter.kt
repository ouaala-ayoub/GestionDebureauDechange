package com.example.gestiondebureaudechangededevises.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Transaction
import com.example.gestiondebureaudechangededevises.data.models.TransactionSchema
import com.example.gestiondebureaudechangededevises.data.models.TransactionType
import com.example.gestiondebureaudechangededevises.databinding.SingleTransactionLayoutBinding
import com.example.gestiondebureaudechangededevises.utils.formatNumberWithCommas

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.TransactionHolder>() {

    private var transactionsList = listOf<TransactionSchema>()
    fun setTransactionsList(list: List<TransactionSchema>) {
        transactionsList = list
        notifyDataSetChanged()
    }

    inner class TransactionHolder(private val binding: SingleTransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val transaction = transactionsList[position]
            val context = binding.root.context
            val isBuy = transaction.type == TransactionType.BUY.value
            val inValue = if (isBuy) {
                context.getString(
                    R.string.montant,
                    formatNumberWithCommas(transaction.amount),
                    transaction.currency
                )
            } else {
                context.getString(
                    R.string.montant,
                    formatNumberWithCommas(transaction.amount / transaction.currencyValue),
                    "MAD"
                )
            }
            val outValue = if (isBuy) {
                context.getString(
                    R.string.montant,
                    formatNumberWithCommas(transaction.amount * transaction.currencyValue),
                    "MAD"
                )
            } else {
                context.getString(
                    R.string.montant,
                    formatNumberWithCommas(transaction.amount),
                    transaction.currency
                )
            }
            binding.apply {
                id.text = context.getString(R.string.id, transaction.id)
                `in`.text = inValue
                out.text = outValue
                taux.text = transaction.currencyValue.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        return TransactionHolder(
            SingleTransactionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = transactionsList.size

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        holder.bind(position)
    }
}