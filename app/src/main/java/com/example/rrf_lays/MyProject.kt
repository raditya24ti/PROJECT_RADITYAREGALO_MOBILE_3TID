package com.example.rrf_lays

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.databinding.ActivityMyProjectBinding

class MyProject : AppCompatActivity() {

    private lateinit var binding: ActivityMyProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMyProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Snack"
        binding.btnKembali.setOnClickListener {
            finish()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
