package com.example.gestiondebureaudechangededevises.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.databinding.ActivityBureauBinding
import com.example.gestiondebureaudechangededevises.utils.DataUpdateListener

class BureauActivity : AppCompatActivity() {

    var bureauDataUpdateListener: DataUpdateListener? = null
    private lateinit var binding: ActivityBureauBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityBureauBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!binding.fragmentContainerView.findNavController().popBackStack()){
            finish()
        }
        return true
    }
}