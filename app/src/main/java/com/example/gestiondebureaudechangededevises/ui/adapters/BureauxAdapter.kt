package com.example.gestiondebureaudechangededevises.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.databinding.SingleBureauLayoutBinding
import com.example.gestiondebureaudechangededevises.utils.showPopUpMenu

class BureauxAdapter(
    private val onBureauClicked: (String) -> Unit,
    private val onDeleteClicked: (String) -> Unit,
    private val onEditClicked: (Bureau) -> Unit,

) :
    RecyclerView.Adapter<BureauxAdapter.BureauHolder>(), Filterable {

    private var bureauxList: List<Bureau> = mutableListOf()
    private var filteredList: List<Bureau> = bureauxList

    fun setBureauxList(list: List<Bureau>) {
        bureauxList = list
        filteredList = bureauxList
        notifyDataSetChanged()
    }

    inner class BureauHolder(private val binding: SingleBureauLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val currentBureau = filteredList[position]
            binding.apply {
                bureauName.setOnClickListener {
                    onBureauClicked(currentBureau.id!!)
                }
                more.setOnClickListener {
                    showPopUpMenu(
                        it,
                        currentBureau,
                        currentBureau.id!!,
                        onEditClicked,
                        onDeleteClicked=onDeleteClicked,
                        menu = R.menu.more_menu_elements
                    )
                }
                bureauWhole.setOnLongClickListener {
                    showPopUpMenu(
                        more,
                        currentBureau,
                        currentBureau.id!!,
                        onEditClicked,
                        onDeleteClicked=onDeleteClicked,
                        menu = R.menu.more_menu_elements
                    )
                    true
                }
                bureauName.text = currentBureau.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BureauHolder {
        return BureauHolder(
            SingleBureauLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: BureauHolder, position: Int) {
        holder.bind(position)
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {

            filteredList = if (query.isNullOrEmpty()) {
                bureauxList
            } else {
                bureauxList.filter { bureau ->
                    //filtering by description
                    bureau.name.contains(query, ignoreCase = true)
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(query: CharSequence?, result: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}