package com.example.gestiondebureaudechangededevises.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import com.example.gestiondebureaudechangededevises.databinding.ActivityUsersBinding
import com.example.gestiondebureaudechangededevises.utils.DataUpdateListener

class UsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsersBinding
    var dataUpdateListener: DataUpdateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!binding.fragmentContainerView2.findNavController().popBackStack()) {
            finish()
        }
        return true
    }
}