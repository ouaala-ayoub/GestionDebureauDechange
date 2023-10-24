package com.example.gestiondebureaudechangededevises.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.databinding.SingleStockLayoutBinding

class StockAdapter : RecyclerView.Adapter<StockAdapter.StockHolder>() {

    private var stock = mapOf<String, String>()

    fun setStock(map: Map<String, String>) {
        stock = map
        notifyDataSetChanged()
    }

    inner class StockHolder(private val binding: SingleStockLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val current = stock.entries.elementAt(position)
            val context = binding.root.context
            binding.apply {
                stockTv.text = context.getString(R.string.montant, current.key, current.value)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        return StockHolder(
            SingleStockLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = stock.size

    override fun onBindViewHolder(holder: StockHolder, position: Int) {
        holder.bind(position)
    }
}
