package com.example.rrf_lays

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.rrf_lays.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data kiriman dari RegisterActivity
        val phone = intent.getStringExtra("EXTRA_PHONE") ?: ""
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: ""
        val password = intent.getStringExtra("EXTRA_PASSWORD") ?: ""

        binding.btnVerifyOtp.setOnClickListener {
            val inputOtp = binding.inputOtp.text.toString().trim()


            if (inputOtp.isNotEmpty() && inputOtp == phone) {

                val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("saved_username", username)
                editor.putString("saved_password", password)
                editor.apply()

                Toast.makeText(this, "Registrasi Berhasil! Silakan Login", Toast.LENGTH_SHORT).show()

                // Kembali ke Halaman Login (AuthActivity)
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
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