package com.example.rrf_lays.pertemuan_5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.R
import com.example.rrf_lays.databinding.ActivityFifthBinding

class FifthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFifthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFifthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar - Pertemuan 5
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Activity Fifth"
            subtitle = "Ini adalah subtitle"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Button WebView - Pertemuan 5
        binding.btnWebView.setOnClickListener {
            val intent = Intent(
                this,
                WebViewActivity::class.java
            )

            startActivity(intent)
        }
    }

    // Option Menu - Pertemuan 5
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Toolbar Back + Option Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_search -> {
                Toast.makeText(
                    this,
                    "Search dipilih",
                    Toast.LENGTH_SHORT
                ).show()

                true
            }

            R.id.action_settings -> {
                Toast.makeText(
                    this,
                    "Settings dipilih",
                    Toast.LENGTH_SHORT
                ).show()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}