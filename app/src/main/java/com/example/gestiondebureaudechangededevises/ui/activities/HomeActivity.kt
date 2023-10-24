package com.example.gestiondebureaudechangededevises.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Admin
import com.example.gestiondebureaudechangededevises.databinding.ActivityHomeBinding
import com.example.gestiondebureaudechangededevises.databinding.HomeNavHeaderBinding
import com.example.gestiondebureaudechangededevises.utils.CurrentAdmin
import com.example.gestiondebureaudechangededevises.utils.OnChanged
import com.example.gestiondebureaudechangededevises.utils.goToActivity
import com.example.gestiondebureaudechangededevises.utils.shortToast

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        binding.apply {
            initialiseDrawerLayout(drawerLayout)
            bureauChoice.setOnClickListener {
                goToActivity<BureauActivity>(this@HomeActivity)
            }
            usersChoice.setOnClickListener {
                goToActivity<UsersActivity>(this@HomeActivity)
            }
        }

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                handleDrawerLayout(binding.drawerLayout)
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initialiseDrawerLayout(drawerLayout: DrawerLayout) {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val headerView = binding.navView.getHeaderView(0)
        val headerViewBinding = HomeNavHeaderBinding.bind(headerView)

        if (CurrentAdmin.isConnected()){
            headerViewBinding.apply {
                name.text = CurrentAdmin.get()?.name
                userName.text = CurrentAdmin.get()?.userName
            }
        }

        CurrentAdmin.observe(this, object : OnChanged<Admin> {
            override fun onChange(data: Admin?) {
                headerViewBinding.apply {
                    name.text = data?.name
                    userName.text = data?.userName
                }
            }

        })

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_admin -> {
                    shortToast("coming soon")
                }
                R.id.logout -> {
                    CurrentAdmin.logout()
                    finish()
                    goToActivity<LoginActivity>(this)
                }
            }
            true
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun handleDrawerLayout(drawerLayout: DrawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}