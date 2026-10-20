package com.example.rrf_lays

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

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
                // Kirim data registrasi ke OtpActivity
                val intent = Intent(this, OtpActivity::class.java).apply {
                    putExtra("EXTRA_NAME", name)
                    putExtra("EXTRA_PHONE", phone)
                    putExtra("EXTRA_USERNAME", username)
                    putExtra("EXTRA_PASSWORD", password)
                }
                startActivity(intent)
            } else {
                binding.inputNama.error = "Semua field harus diisi!"
            }
        }
    }
}