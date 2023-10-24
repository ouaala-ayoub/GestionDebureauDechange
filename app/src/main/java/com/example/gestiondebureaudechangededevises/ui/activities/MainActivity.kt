package com.example.gestiondebureaudechangededevises.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.example.gestiondebureaudechangededevises.data.repositories.AuthRepository
import com.example.gestiondebureaudechangededevises.databinding.ActivityMainBinding
import com.example.gestiondebureaudechangededevises.ui.viewmodels.MainActivityViewModel
import com.example.gestiondebureaudechangededevises.utils.CurrentAdmin
import com.example.gestiondebureaudechangededevises.utils.goToActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private val mainModel: MainActivityViewModel =
        MainActivityViewModel(AuthRepository.getInstance()).also {
            it.getAuth()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        mainModel.apply {
            val context = this@MainActivity
            loading.observe(context) { loading ->
                binding.progressBar.isVisible = loading
            }
            admin.observe(context) { admin ->
                Log.d(TAG, "admin: $admin")
                if (admin != null) {
                    CurrentAdmin.set(admin)
                    goToActivity<HomeActivity>(context)
                } else {
                    goToActivity<LoginActivity>(context)
                }
            }
        }

        setContentView(binding.root)
    }
}