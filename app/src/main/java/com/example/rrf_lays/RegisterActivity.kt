package com.example.rrf_lays

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rrf_lays.data.api.ApiErrorHelper
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.api.UserApiService
import com.example.rrf_lays.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userApi by lazy { SupabaseClient.create<UserApiService>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitRegister.setOnClickListener {
            val name = binding.inputNama.text.toString().trim()
            val phone = binding.inputPhone.text.toString().trim()
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                binding.btnSubmitRegister.isEnabled = false
                binding.btnSubmitRegister.text = "Memeriksa..."

                lifecycleScope.launch {
                    try {
                        val response = userApi.findByUsername("eq.$username")
                        if (response.isSuccessful) {
                            val list = response.body()
                            if (!list.isNullOrEmpty()) {
                                binding.inputUsername.error = "Username sudah dipakai"
                                Toast.makeText(this@RegisterActivity, "Username sudah dipakai", Toast.LENGTH_SHORT).show()
                            } else {
                                // Lanjut ke OtpActivity
                                val intent = Intent(this@RegisterActivity, OtpActivity::class.java).apply {
                                    putExtra("EXTRA_NAME", name)
                                    putExtra("EXTRA_PHONE", phone)
                                    putExtra("EXTRA_USERNAME", username)
                                    putExtra("EXTRA_PASSWORD", password)
                                }
                                startActivity(intent)
                            }
                        } else {
                            val errorMsg = ApiErrorHelper.parseError(response)
                            Toast.makeText(this@RegisterActivity, errorMsg, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Supabase", "gagal", e)
                        Toast.makeText(this@RegisterActivity, "Gagal terhubung: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        binding.btnSubmitRegister.isEnabled = true
                        binding.btnSubmitRegister.text = "Lanjut Verifikasi OTP"
                    }
                }
            } else {
                binding.inputNama.error = "Semua field harus diisi!"
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
