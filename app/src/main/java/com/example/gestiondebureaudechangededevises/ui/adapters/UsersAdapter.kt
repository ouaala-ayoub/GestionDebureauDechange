package com.example.gestiondebureaudechangededevises.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.databinding.SingleUserLayoutBinding
import com.example.gestiondebureaudechangededevises.utils.showPopUpMenu

class UsersAdapter(
    private val onUserClicked: (String) -> Unit,
    private val onDeleteClicked: (String) -> Unit,
    private val onEditClicked: (User) -> Unit,
    private val onEditCredentialsClicked: (String) -> Unit,
) : RecyclerView.Adapter<UsersAdapter.UserHolder>(), Filterable {

    private var usersList: List<User> = mutableListOf()
    private var filteredList: List<User> = usersList

    fun setUsersList(list: List<User>) {
        usersList = list
        filteredList = usersList
        notifyDataSetChanged()
    }

    inner class UserHolder(private val binding: SingleUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val currentUser = filteredList[position]
            val context = binding.root.context
            binding.apply {
                userName.setOnClickListener {
                    onUserClicked(currentUser.id!!)
                }
                more.setOnClickListener {
                    showPopUpMenu(
                        it,
                        currentUser,
                        currentUser.id!!,
                        onEditClicked,
                        onEditCredentialsClicked,
                        onDeleteClicked,
                        R.menu.user_more_menu_elements
                    )
                }
                userWhole.setOnLongClickListener {
                    showPopUpMenu(
                        more,
                        currentUser,
                        currentUser.id!!,
                        onEditClicked,
                        onEditCredentialsClicked,
                        onDeleteClicked,
                        R.menu.user_more_menu_elements
                    )
                    true
                }
                userName.text = currentUser.name
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserHolder {
        return UserHolder(
            SingleUserLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: UsersAdapter.UserHolder, position: Int) {
        holder.bind(position)
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {

            filteredList = if (query.isNullOrEmpty()) {
                usersList
            } else {
                usersList.filter { user ->
                    //filtering by description
                    user.name.contains(query, ignoreCase = true)
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