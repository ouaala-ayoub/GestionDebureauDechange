package com.example.gestiondebureaudechangededevises.ui.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Bureau
import com.example.gestiondebureaudechangededevises.data.models.Stock
import com.example.gestiondebureaudechangededevises.data.models.currencyCodes
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.databinding.ActivityAddBureauBinding
import com.example.gestiondebureaudechangededevises.ui.adapters.CurrencyInputAdapter
import com.example.gestiondebureaudechangededevises.ui.viewmodels.AddBureauActivityViewModel
import com.example.gestiondebureaudechangededevises.utils.*

class AddBureauActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "AddBureauActivity"
    }

    private lateinit var binding: ActivityAddBureauBinding
    private lateinit var currencyAdapter: CurrencyInputAdapter
    private val viewModel = AddBureauActivityViewModel(BureauRepository.getInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val emptyCurrenciesMap = currencyCodes.zip(List(
            currencyCodes.size
        ) { "0" }).toMap()
        val dummyView = layoutInflater.inflate(R.layout.loading_page, null, false)
        val asyncInflater = AsyncLayoutInflater(this)

        currencyAdapter = CurrencyInputAdapter(viewModel.currencies, emptyCurrenciesMap)

        asyncInflater.inflate(R.layout.activity_add_bureau, null) { inflatedView, _, _ ->
            binding = ActivityAddBureauBinding.bind(inflatedView)
            (dummyView as ViewGroup).apply {
                removeAllViews()
                addView(binding.root)
            }
            binding.apply {
                stockRv.apply {
                    adapter = currencyAdapter
                    layoutManager = LinearLayoutManager(this@AddBureauActivity)
                    setHasFixedSize(true)
                }
                viewModel.apply {
                    //Views
                    nameEditText.updateLiveData { name ->
                        setName(name)
                    }

                    addBureau.setOnClickListener {

                        val dialog = makeDialog(
                            this@AddBureauActivity,
                            object : OnDialogClicked {
                                override fun onPositiveButtonClicked() {
                                    val name = name.value.toString()
                                    val stock = viewModel.getStock()
                                    Log.d(TAG, "stock: $stock")
                                    val bureau = Bureau(
                                        name = name, stock = stock
                                    )
                                    addBureau(bureau)
                                }

                                override fun onNegativeButtonClicked() {}

                            },
                            title = getString(R.string.add_bureau_title),
                            message = getString(R.string.add_bureau_message),
                        )
                        dialog.show()
                    }

                    //livedata observing
                    loading.observe(this@AddBureauActivity) { loading ->
                        progressBar.isVisible = loading
                        blockUi(loading)
                    }
                    added.observe(this@AddBureauActivity) { bureau ->
                        if (bureau != null) {
                            Log.d(TAG, "added bureau id: ${bureau.id}")
                            doOnAdded()
                        } else {
                            shortToast(getString(R.string.error))
                        }
                    }
                    validData.observe(this@AddBureauActivity) { valid ->
                        addBureau.isEnabled = valid
                    }
                }
            }
        }

        setContentView(dummyView)
    }

    private fun blockUi(loading: Boolean) {
        binding.apply {
            for (v in scrollView.children) {
                v.isEnabled = !loading
            }
            addBureau.isEnabled = !loading
        }
    }

    private fun doOnAdded() {
        shortToast(getString(R.string.added_bureau))
        intent.putExtra("added", true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}