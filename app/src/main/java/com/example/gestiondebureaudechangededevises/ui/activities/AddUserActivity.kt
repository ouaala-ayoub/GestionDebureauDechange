package com.example.gestiondebureaudechangededevises.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.databinding.ActivityAddUserBinding
import com.example.gestiondebureaudechangededevises.ui.viewmodels.AddUserActivityViewModel
import com.example.gestiondebureaudechangededevises.utils.setWithList
import com.example.gestiondebureaudechangededevises.utils.shortToast
import com.example.gestiondebureaudechangededevises.utils.updateLiveData


class AddUserActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddUserActivity"
    }

    private lateinit var binding: ActivityAddUserBinding
    private val viewModel =
        AddUserActivityViewModel(
            UserRepository.getInstance(),
            BureauRepository.getInstance()
        ).also {
            it.getBureaux()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        binding.apply {
            viewModel.apply {
                nameEditText.updateLiveData {
                    setName(it)
                }
                userNameEditText.updateLiveData {
                    setUserName(it)
                }
                passwordEditText.updateLiveData {
                    setPassword(it)
                }
                retypePasswordEditText.updateLiveData {
                    setRetypedPassword(it)
                }
                addUser.setOnClickListener {
                    val user = User(
                        name = name.value.toString(),
                        email = emailEditText.text.toString(),
                        phone = phoneEditText.text.toString(),
                        userName = userName.value.toString(),
                        password = password.value.toString(),
                        desk = bureau.value.toString()
                    )
                    Log.d(TAG, "user: $user")
                    addUser(user)
                }
                loading.observe(this@AddUserActivity) { loading ->
                    Log.d(TAG, "loading: $loading")
                    progressBar.isVisible = loading
                }
                requestLoading.observe(this@AddUserActivity) { loading ->
                    progressBar.isVisible = loading
                    blockUi(loading)
                }
                added.observe(this@AddUserActivity) { user ->
                    if (user != null) {
                        Log.d(TAG, "added user id: ${user.id}")
                        doOnAdded()
                    } else {
                        shortToast(getString(R.string.error))
                    }
                }
                bureaux.observe(this@AddUserActivity) { bureaux ->
                    Log.d(TAG, "bureaux: $bureaux")
                    if (bureaux != null) {
                        bureauEditText.apply {
                            setWithList(bureaux.map { bureau -> bureau.name })
                            setOnItemClickListener { _, _, i, _ ->
                                Log.d(TAG, "setOnItemClickListener: ${bureaux[i].id!!}")
                                setBureau(bureaux[i].id!!)
                            }
                        }
                    } else {
                        shortToast(getString(R.string.error))
                        finish()
                    }
                }
                validData.observe(this@AddUserActivity) { valid ->
                    Log.d(TAG, "valid: $valid")
                    addUser.isEnabled = valid
                }
            }
        }
        setContentView(binding.root)
    }

    private fun blockUi(loading: Boolean) {
        for (v in binding.linearLayout.children) {
            v.isEnabled = !loading
        }
        binding.addUser.isEnabled = !loading
    }

    private fun doOnAdded() {
        shortToast(getString(R.string.added_user))
        intent.putExtra("added", true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}