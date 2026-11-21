package com.example.rrf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rrf_lays.data.api.ApiErrorHelper
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.api.UserApiService
import com.example.rrf_lays.databinding.ActivityAuthBinding
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val userApi by lazy { SupabaseClient.create<UserApiService>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil SharedPreferences
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Navigasi dari Login ke Halaman Registrasi
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Logika Login dengan Rule 1 & Rule 2 (Supabase)
        binding.btnLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username dan Password tidak boleh kosong.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            // Rule 1: username == password (tetap dipertahankan)
            val isRule1Valid = username == password

            if (isRule1Valid) {
                // Simpan status login
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                editor.apply()

                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            // Rule 2: Cek ke Supabase (mobile.users) tanpa fallback SharedPreferences untuk sementara
            binding.btnLogin.isEnabled = false
            binding.btnLogin.text = "Memeriksa..."

            lifecycleScope.launch {
                var loginSuccess = false
                var fetchedName: String? = null
                var errorMessage: String? = null

                try {
                    val response = userApi.login("eq.$username", "eq.$password")
                    if (response.isSuccessful) {
                        val users = response.body()
                        if (!users.isNullOrEmpty()) {
                            loginSuccess = true
                            fetchedName = users[0].nama
                        } else {
                            errorMessage = "Username atau Password salah atau tidak terdaftar di Supabase."
                        }
                    } else {
                        errorMessage = ApiErrorHelper.parseError(response)
                    }
                } catch (e: Exception) {
                    Log.e("Supabase", "gagal", e)
                    errorMessage = "Gagal terhubung ke server: ${e.message}"
                } finally {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Login"
                }

                if (loginSuccess) {
                    val editor = sharedPref.edit()
                    editor.putBoolean("isLogin", true)
                    editor.putString("username", username)
                    if (fetchedName != null) {
                        editor.putString("nama", fetchedName)
                    }
                    editor.apply()

                    val intent = Intent(this@AuthActivity, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    AlertDialog.Builder(this@AuthActivity)
                        .setTitle("Login Gagal")
                        .setMessage(errorMessage ?: "Username atau Password salah.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }
}
