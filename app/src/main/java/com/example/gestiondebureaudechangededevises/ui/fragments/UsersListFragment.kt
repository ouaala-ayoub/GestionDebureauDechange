package com.example.gestiondebureaudechangededevises.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.databinding.FragmentUsersListBinding
import com.example.gestiondebureaudechangededevises.ui.activities.AddUserActivity
import com.example.gestiondebureaudechangededevises.ui.activities.BureauActivity
import com.example.gestiondebureaudechangededevises.ui.activities.UsersActivity
import com.example.gestiondebureaudechangededevises.ui.adapters.UsersAdapter
import com.example.gestiondebureaudechangededevises.ui.viewmodels.UsersListFragmentViewModel
import com.example.gestiondebureaudechangededevises.utils.*

class UsersListFragment : Fragment() {

    companion object {
        private const val TAG = "UsersListFragment"
    }

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!
    private val usersAdapter: UsersAdapter =
        UsersAdapter(::goToUserPage, ::deleteUser, ::goToUserEdit, ::goToEditCredentials)

    private val viewModel: UsersListFragmentViewModel =
        UsersListFragmentViewModel(UserRepository.getInstance()).also {
            it.getUsers()
        }
    private val addUserActivityLauncher = startActivityResult(object : SelectionResult {
        override fun onResultOk(data: Intent) {
            val added = data.getBooleanExtra("added", false)
            Log.d(TAG, "added: $added")
            if (added) {
                viewModel.getUsers()
            }
        }

        override fun onResultFailed() {
            Log.i(TAG, "no bureau added")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as UsersActivity).dataUpdateListener = object : DataUpdateListener {
            override fun onDataUpdated() {
                viewModel.getUsers()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)

        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Handle query submission (if needed)
                    filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }

                fun filter(query: String?) {
                    usersAdapter.filter.filter(query.orEmpty())
                }
            })
            usersRv.apply {
                adapter = usersAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.getUsers()
            }

            addUser.setOnClickListener {
                requireContext().launchActivityForResult<AddUserActivity>(
                    addUserActivityLauncher
                )
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            deleted.observe(viewLifecycleOwner) { deleted ->
                deleted?.apply {
                    viewModel.getUsers()
                }
            }
            loading.observe(viewLifecycleOwner) { loading ->
                binding.usersProgressBar.isVisible = loading
            }
            list.observe(viewLifecycleOwner) { list ->
                Log.d(TAG, "bureaux: $list")
                binding.swipeRefresh.isRefreshing = false
                if (!list.isNullOrEmpty()) {
                    usersAdapter.setUsersList(list)
                    setMessage("")
                } else if (list == null) {
                    setMessage(getString(R.string.error))
                } else if (list.isEmpty()) {
                    setMessage(getString(R.string.empty_users))
                }

            }

            message.observe(viewLifecycleOwner) { message ->
                binding.message.text = message
            }
        }
    }

    private fun goToUserPage(userId: String) {
        val action = UsersListFragmentDirections.actionUsersListFragmentToUserPageFragment(userId)
        findNavController().navigate(action)
    }

    private fun goToEditCredentials(userId: String) {
        val action = UsersListFragmentDirections.actionUsersListFragmentToPasswordChangeFragment2(userId)
        findNavController().navigate(action)
    }

    private fun deleteUser(userId: String) {
        val dialog = makeDialog(
            requireContext(),
            object : OnDialogClicked {
                override fun onPositiveButtonClicked() {
                    viewModel.deleteUser(userId)
                }

                override fun onNegativeButtonClicked() {}

            },
            title = getString(R.string.delete_bureau_title),
            message = getString(R.string.delete_user_message)
        )
        dialog.show()
    }

    private fun goToUserEdit(user: User) {
        val action = UsersListFragmentDirections.actionUsersListFragmentToUserEditFragment(user)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as UsersActivity).dataUpdateListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}