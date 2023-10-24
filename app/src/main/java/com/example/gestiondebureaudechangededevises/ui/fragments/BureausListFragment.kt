package com.example.gestiondebureaudechangededevises.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.databinding.FragmentBureausListBinding
import com.example.gestiondebureaudechangededevises.ui.activities.AddBureauActivity
import com.example.gestiondebureaudechangededevises.ui.adapters.BureauxAdapter
import com.example.gestiondebureaudechangededevises.ui.viewmodels.BureausListViewModel
import com.example.gestiondebureaudechangededevises.utils.*
import android.widget.SearchView
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.ui.activities.BureauActivity
import com.example.gestiondebureaudechangededevises.ui.activities.UsersActivity

class BureausListFragment : Fragment() {

    companion object {
        private const val TAG = "BureausListFragment"
    }

    private var _binding: FragmentBureausListBinding? = null
    private val binding get() = _binding!!
    private val bureauxAdapter: BureauxAdapter =
        BureauxAdapter(::goToBureauPage, ::deleteBureau, ::goToBureauEdit)
    private val viewModel: BureausListViewModel =
        BureausListViewModel(BureauRepository.getInstance()).also {
            it.getBureaux()
        }
    private val addBureauActivityLauncher = startActivityResult(object : SelectionResult {
        override fun onResultOk(data: Intent) {
            val added = data.getBooleanExtra("added", false)
            Log.d(TAG, "added: $added")
            if (added) {
                viewModel.getBureaux()
            }
        }

        override fun onResultFailed() {
            Log.i(TAG, "no bureau added")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as BureauActivity).bureauDataUpdateListener =
            object : DataUpdateListener {
                override fun onDataUpdated() {
                    viewModel.getBureaux()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBureausListBinding.inflate(inflater, container, false)

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
                    bureauxAdapter.filter.filter(query.orEmpty())
                }
            })
            bureauRv.apply {
                adapter = bureauxAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.getBureaux()
            }

            addBureau.setOnClickListener {
                requireContext().launchActivityForResult<AddBureauActivity>(
                    addBureauActivityLauncher
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
                    viewModel.getBureaux()
                }
            }
            loading.observe(viewLifecycleOwner) { loading ->
                binding.bureauProgressBar.isVisible = loading
            }
            list.observe(viewLifecycleOwner) { list ->
                Log.d(TAG, "bureaux: $list")
                binding.swipeRefresh.isRefreshing = false
                if (!list.isNullOrEmpty()) {
                    bureauxAdapter.setBureauxList(list)
                    setMessage("")
                } else if (list == null) {
                    setMessage(getString(R.string.error))
                } else if (list.isEmpty()) {
                    setMessage(getString(R.string.empty_desks))
                }

            }

            message.observe(viewLifecycleOwner) { message ->
                binding.message.text = message
            }
        }
    }

    private fun goToBureauPage(bureauId: String) {
        val action = BureausListFragmentDirections.actionBureausListToBureauPageFragment(bureauId)
        findNavController().navigate(action)
    }

    private fun deleteBureau(bureauId: String) {
        val dialog = makeDialog(
            requireContext(),
            object : OnDialogClicked {
                override fun onPositiveButtonClicked() {
                    viewModel.deleteBureau(bureauId)
                }

                override fun onNegativeButtonClicked() {}

            },
            title = getString(R.string.delete_bureau_title),
            message = getString(R.string.delete_bureau_message)
        )
        dialog.show()
    }

    private fun goToBureauEdit(bureau: Bureau) {
        val action =
            BureausListFragmentDirections.actionBureausListFragmentToBureauEditFragment2(bureau)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as BureauActivity).bureauDataUpdateListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}