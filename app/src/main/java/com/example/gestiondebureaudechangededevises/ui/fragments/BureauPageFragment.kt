package com.example.gestiondebureaudechangededevises.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.data.repositories.TransactionRepository
import com.example.gestiondebureaudechangededevises.databinding.FragmentBureauPageBinding
import com.example.gestiondebureaudechangededevises.ui.adapters.StockAdapter
import com.example.gestiondebureaudechangededevises.ui.adapters.TransactionsAdapter
import com.example.gestiondebureaudechangededevises.ui.viewmodels.BureauPageViewModel
import com.example.gestiondebureaudechangededevises.utils.shortToast
import java.util.stream.Stream

class BureauPageFragment : Fragment() {

    companion object {
        private const val TAG = "BureauPageFragment"
    }

    private lateinit var binding: FragmentBureauPageBinding
    private val transactionAdapter: TransactionsAdapter = TransactionsAdapter()
    private val stockAdapter: StockAdapter = StockAdapter()
    private val viewModel: BureauPageViewModel = BureauPageViewModel(
        BureauRepository.getInstance(),
        TransactionRepository.getInstance()
    )
    private val args: BureauPageFragmentArgs by navArgs()
    private lateinit var bureauId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bureauId = args.bureauId
        viewModel.apply {
            getBureauById(bureauId)
            getTransactions(bureauId)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBureauPageBinding.inflate(inflater, container, false)

        binding.apply {

            stockRv.apply {
                adapter = stockAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            //disable refresh if not in the top of the scroll view
            scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val isAtTop = scrollY == 0
                swipeRefresh.isEnabled =
                    isAtTop && !transactionsRv.canScrollVertically(-1)
            }

            transactionsRv.apply {
                adapter = transactionAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.apply {
                    getBureauById(bureauId)
                    getTransactions(bureauId)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            bureau.observe(viewLifecycleOwner) { bureau ->
                if (bureau != null) {

                    binding.apply {
                        textViewName.text = getString(R.string.bureau_name, bureau.name)
                        stockAdapter.setStock(bureau.stock!!)
                    }

                } else {
                    requireContext().shortToast(getString(R.string.error))
                    findNavController().popBackStack()
                }
            }

            loading.observe(viewLifecycleOwner) { loading ->
                binding.bureauPageProgressbar.isVisible = loading
            }
            list.observe(viewLifecycleOwner) { list ->
                binding.swipeRefresh.isRefreshing = false
                if (!list.isNullOrEmpty()) {
                    transactionAdapter.setTransactionsList(list)
                    setMessage("")
                } else if (list == null) {
                    setMessage(getString(R.string.error))
                } else if (list.isEmpty()) {
                    setMessage(getString(R.string.empty_transactions))
                }

            }

            message.observe(viewLifecycleOwner) { message ->
                binding.message.text = message
            }
        }
    }

}