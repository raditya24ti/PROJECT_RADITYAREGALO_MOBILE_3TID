package com.example.rrf_lays

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences
        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        binding.btnLogin.setOnClickListener {

            val username = binding.inputUsername.text.toString()
            val password = binding.inputPassword.text.toString()

            if (username == password) {

                // Simpan status login dan username
                val editor = sharedPref.edit()

                editor.putBoolean(
                    "isLogin",
                    true
                )

                editor.putString(
                    "username",
                    username
                )

                editor.apply()

                // Login berhasil → AdminActivity
                val intent = Intent(
                    this,
                    AdminActivity::class.java
                )

                startActivity(intent)
                finish()

            } else {

                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Silahkan coba lagi")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}