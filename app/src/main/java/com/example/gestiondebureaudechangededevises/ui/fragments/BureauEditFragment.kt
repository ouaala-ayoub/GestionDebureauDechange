package com.example.gestiondebureaudechangededevises.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.databinding.FragmentBureauEditBinding
import com.example.gestiondebureaudechangededevises.ui.activities.AddBureauActivity
import com.example.gestiondebureaudechangededevises.ui.activities.BureauActivity
import com.example.gestiondebureaudechangededevises.ui.adapters.CurrencyInputAdapter
import com.example.gestiondebureaudechangededevises.ui.viewmodels.BureauEditViewModel
import com.example.gestiondebureaudechangededevises.utils.*

class BureauEditFragment : Fragment() {

    companion object {
        private const val TAG = "BureauEditFragment"
    }

    private lateinit var viewModel: BureauEditViewModel
    private var _binding: FragmentBureauEditBinding? = null
    private val binding get() = _binding!!
    private val args: BureauEditFragmentArgs by navArgs()
    private lateinit var currenciesAdapter: CurrencyInputAdapter
    private lateinit var bureauToEdit: Bureau

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bureauToEdit = args.bureau
        viewModel = BureauEditViewModel(BureauRepository.getInstance(), bureauToEdit)
        currenciesAdapter = CurrencyInputAdapter(viewModel.currencies, bureauToEdit.stock!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dummyView = inflater.inflate(R.layout.loading_page, container, false)
        val asyncInflater = AsyncLayoutInflater(requireContext())

        asyncInflater.inflate(R.layout.fragment_bureau_edit, null) { inflatedView, _, _ ->
            _binding = FragmentBureauEditBinding.bind(inflatedView)

            if (view != null){

                binding.apply {
                    currencyInputsRv.apply {
                        adapter = currenciesAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                    }
                    viewModel.apply {
                        nameEditText.updateLiveData { name ->
                            setName(name)
                        }

                        updateBureau.setOnClickListener {


                            val dialog = makeDialog(
                                requireContext(),
                                object : OnDialogClicked {
                                    override fun onPositiveButtonClicked() {
                                        val stock = viewModel.getStock()
                                        Log.d(TAG, "stock: $stock")
                                        val bureau = Bureau(
                                            name = name.value.toString(),
                                            stock = viewModel.getStock()
                                        )
                                        putBureau(bureauToEdit.id!!, bureau)
                                    }

                                    override fun onNegativeButtonClicked() {}

                                },
                                title = getString(R.string.add_bureau_title),
                                message = getString(R.string.add_bureau_message),
                            )
                            dialog.show()

                        }
                    }

                }
                initialiseViews(bureauToEdit)
                (dummyView as ViewGroup).apply {
                    removeAllViews()
                    addView(binding.root)
                }
                viewModel.apply {
                    loading.observe(viewLifecycleOwner) { loading ->
                        binding.progressBar.isVisible = loading
                        blockUi(loading!!)
                    }
                    updated.observe(viewLifecycleOwner) { message ->
                        if (message != null) {
                            requireContext().shortToast(message)
                            (requireActivity() as BureauActivity).bureauDataUpdateListener?.onDataUpdated()
                        } else {
                            requireContext().shortToast(getString(R.string.error))
                        }
                    }
                    validData.observe(viewLifecycleOwner) { valid ->
                        binding.updateBureau.isEnabled = valid
                    }
                }
            }

        }

        return dummyView
    }

    private fun initialiseViews(bureau: Bureau) {
        binding.apply {
            nameEditText.setText(bureau.name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun blockUi(loading: Boolean) {
        binding.apply {
            for (v in scrollView.children) {
                v.isEnabled = !loading
            }
            updateBureau.isEnabled = !loading
        }
    }
}