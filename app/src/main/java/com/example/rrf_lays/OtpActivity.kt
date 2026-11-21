package com.example.rrf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rrf_lays.data.api.ApiErrorHelper
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.api.UserApiService
import com.example.rrf_lays.data.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.rrf_lays.databinding.ActivityOtpBinding
import kotlinx.coroutines.launch

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private val userApi by lazy { SupabaseClient.create<UserApiService>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data kiriman dari RegisterActivity
        val name = intent.getStringExtra("EXTRA_NAME") ?: ""
        val phone = intent.getStringExtra("EXTRA_PHONE") ?: ""
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: ""
        val password = intent.getStringExtra("EXTRA_PASSWORD") ?: ""

        binding.btnVerifyOtp.setOnClickListener {
            val inputOtp = binding.inputOtp.text.toString().trim()

            if (inputOtp.isNotEmpty() && inputOtp == phone) {
                binding.btnVerifyOtp.isEnabled = false
                binding.btnVerifyOtp.text = "Menyimpan..."

                lifecycleScope.launch {
                    try {
                        val newUser = User(
                            nama = name,
                            no_hp = phone,
                            username = username,
                            password = password
                        )
                        val response = userApi.register(newUser)

                        // Pastikan HANYA sukses jika response.isSuccessful (kode 201)
                        if (response.isSuccessful) {
                            val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("saved_username", username)
                            editor.putString("saved_password", password)
                            editor.apply()

                            Toast.makeText(this@OtpActivity, "Registrasi Berhasil! Silakan Login", Toast.LENGTH_SHORT).show()

                            // Kembali ke Halaman Login (AuthActivity)
                            val intent = Intent(this@OtpActivity, AuthActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMsg = ApiErrorHelper.parseError(response)
                            MaterialAlertDialogBuilder(this@OtpActivity)
                                .setTitle("Registrasi Gagal")
                                .setMessage(errorMsg)
                                .setPositiveButton("Coba Lagi", null)
                                .show()
                            binding.btnVerifyOtp.isEnabled = true
                            binding.btnVerifyOtp.text = "Verifikasi OTP"
                        }
                    } catch (e: Exception) {
                        Log.e("Supabase", "gagal", e)
                        MaterialAlertDialogBuilder(this@OtpActivity)
                            .setTitle("Koneksi Gagal")
                            .setMessage("Terjadi kesalahan jaringan: ${e.message}")
                            .setPositiveButton("Coba Lagi", null)
                            .show()
                        binding.btnVerifyOtp.isEnabled = true
                        binding.btnVerifyOtp.text = "Verifikasi OTP"
                    }
                }
            } else {
                // Tampilkan Error dengan MaterialAlertDialog
                MaterialAlertDialogBuilder(this)
                    .setTitle("Verifikasi Gagal")
                    .setMessage("Kode OTP yang diinputkan salah atau kosong! Kode OTP harus sama dengan nomor handphone.")
                    .setPositiveButton("Coba Lagi", null)
                    .show()
            }
        }
    }
}
