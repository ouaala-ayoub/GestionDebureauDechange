package com.example.gestiondebureaudechangededevises.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondebureaudechangededevises.databinding.SingleCurrencyInputBinding
import com.example.gestiondebureaudechangededevises.ui.viewmodels.AddBureauActivityViewModel
import com.example.gestiondebureaudechangededevises.ui.viewmodels.BureauEditViewModel
import com.example.gestiondebureaudechangededevises.utils.NumberTextWatcher
import com.example.gestiondebureaudechangededevises.utils.updateLiveData

class CurrencyInputAdapter(
    private val listLiveData: List<MutableLiveData<String?>>,
    private val currenciesMap: Map<String, String>
) :
    RecyclerView.Adapter<CurrencyInputAdapter.CurrencyInputHolder>() {

    inner class CurrencyInputHolder(private val binding: SingleCurrencyInputBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val currentElement = currenciesMap.entries.elementAt(position)
            binding.apply {

                currencyEditText.apply {
                    currencyTextField.hint = currentElement.key
                    setText(currentElement.value)
                    addTextChangedListener(
                        NumberTextWatcher(
                            this,
                            listLiveData[position]
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyInputHolder {
        return CurrencyInputHolder(
            SingleCurrencyInputBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = currenciesMap.size

    override fun onBindViewHolder(holder: CurrencyInputHolder, position: Int) {
        holder.bind(position)
    }

}