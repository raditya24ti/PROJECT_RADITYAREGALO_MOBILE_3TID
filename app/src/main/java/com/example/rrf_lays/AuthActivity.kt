package com.example.rrf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.databinding.ActivityAuthBinding
import kotlin.jvm.java

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil SharedPreferences
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // SOAL 1: Navigasi dari Login ke Halaman Registrasi
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // SOAL 2: Logika Login dengan 2 Aturan (Rule 1 & Rule 2)
        binding.btnLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            // Ambil data yang tersimpan dari SharedPreferences
            val savedUsername = sharedPref.getString("saved_username", null)
            val savedPassword = sharedPref.getString("saved_password", null)

            // Rule 1: username == password
            val isRule1Valid = username.isNotEmpty() && username == password

            // Rule 2: username == username di SharedPreference DAN password == password di SharedPreference
            val isRule2Valid = username.isNotEmpty() &&
                    username == savedUsername &&
                    password == savedPassword

            // Cek apakah memenuhi Rule 1 ATAU Rule 2
            if (isRule1Valid || isRule2Valid) {
                // Simpan status login
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                editor.apply()

                // Login berhasil → Pindah ke AdminActivity
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Jika kedua rule gagal
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username atau Password tidak cocok dengan sistem/SharedPreferences. Silahkan coba lagi.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}