package com.example.rrf_lays

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.rrf_lays.databinding.ActivityBaseBinding
class BaseActivity : AppCompatActivity() {

    // 1. Deklarasi binding
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. Inisialisasi binding
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Penyesuaian padding (bottom diset 0 agar navigasi menempel di bawah)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Ubah systemBars.bottom menjadi 0
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // 4. Tampilkan HomeFragment pertama kali
        replaceFragment(HomeFragment())

        // 5. Logika ketika menu Bottom Navigation di-klik
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.katalog -> {
                    replaceFragment(KatalogFragment())
                    true
                }
                R.id.outlet -> {
                    replaceFragment(OutletFragment())
                    true
                }
                R.id.informasi -> {
                    replaceFragment(InformasiFragment())
                    true
                }
                else -> false
            }
        }
    }

    // 6. Fungsi untuk mengganti fragment di dalam container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}