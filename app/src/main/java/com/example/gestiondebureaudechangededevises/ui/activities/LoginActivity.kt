package com.example.gestiondebureaudechangededevises.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.gestiondebureaudechangededevises.data.models.AdminCredentials
import com.example.gestiondebureaudechangededevises.data.repositories.AuthRepository
import com.example.gestiondebureaudechangededevises.databinding.ActivityLoginBinding
import com.example.gestiondebureaudechangededevises.ui.viewmodels.LoginActivityViewModel
import com.example.gestiondebureaudechangededevises.utils.CurrentAdmin
import com.example.gestiondebureaudechangededevises.utils.goToActivity
import com.example.gestiondebureaudechangededevises.utils.updateLiveData

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginActivityViewModel =
        LoginActivityViewModel(AuthRepository.getInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.apply {
            viewModel.apply {
                editTextEmail.updateLiveData(::setUserName)
                editTextPassword.updateLiveData(::setPassword)

                loading.observe(this@LoginActivity) { loading ->
                    loginProgressBar.isVisible = loading
                    blockUi(loading)
                }

                isValidData.observe(this@LoginActivity) { valid ->
                    buttonLogin.isEnabled = valid
                }

                admin.observe(this@LoginActivity) { admin ->
                    admin?.apply {
                        CurrentAdmin.set(admin)
                        goToActivity<HomeActivity>(this@LoginActivity)
                    }
                }

                helperMessage.observe(this@LoginActivity) { helperMessage ->
                    Log.d(TAG, "helperMessage: $helperMessage")
                    textInputLayoutPassword.helperText = helperMessage
                }

                buttonLogin.setOnClickListener {
                    val userCredentials =
                        AdminCredentials(userName.value.toString(), password.value.toString())
                    Log.d(TAG, "userCredentials: $userCredentials")
                    login(userCredentials)
                }

            }
        }

        setContentView(binding.root)
    }

    private fun blockUi(loading: Boolean) {
        for (view in binding.linearLayout.children) {
            view.isEnabled = !loading
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}