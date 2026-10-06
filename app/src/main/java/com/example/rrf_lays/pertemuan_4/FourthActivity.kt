package com.example.rrf_lays.pertemuan_4

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rrf_lays.databinding.ActivityFourthBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class FourthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFourthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // =========================
        // TOOLBAR
        // =========================

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Rengginang Sabit"
            subtitle = "Pertemuan 4"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // =========================
        // WINDOW INSETS
        // =========================

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // =========================
        // INTENT DATA
        // =========================

        val name = intent.getStringExtra("name")
        val from = intent.getStringExtra("from")
        val age = intent.getIntExtra("age", 0)

        Log.e(
            "Data Intent",
            "Nama: $name, Usia: $age, Asal: $from"
        )

        // =========================
        // SNACKBAR
        // Pertemuan 4
        // =========================

        binding.btnShowSnackbar.setOnClickListener {

            Snackbar.make(
                binding.root,
                "Ini adalah Snackbar",
                Snackbar.LENGTH_SHORT
            )
                .setAction("Tutup") {

                    Log.e(
                        "Info Snackbar",
                        "Snackbar ditutup"
                    )
                }
                .show()
        }

        // =========================
        // ALERT DIALOG
        // Pertemuan 4
        // =========================

        binding.btnShowAlertDialog.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi")
                .setMessage(
                    "Apakah Anda yakin ingin melanjutkan?"
                )
                .setPositiveButton("Ya") { dialog, _ ->

                    dialog.dismiss()

                    Log.e(
                        "Info Dialog",
                        "Anda memilih Ya!"
                    )
                }
                .setNegativeButton("Batal") { dialog, _ ->

                    dialog.dismiss()

                    Log.e(
                        "Info Dialog",
                        "Anda memilih Tidak!"
                    )
                }
                .show()
        }

        // =========================
        // LIFECYCLE
        // =========================

        Log.e(
            "onCreate",
            "FourthActivity dibuat pertama kali"
        )
    }

    // =========================
    // TOOLBAR BACK
    // =========================

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {

        return when (item.itemId) {

            android.R.id.home -> {

                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // =========================
    // ON START
    // =========================

    override fun onStart() {
        super.onStart()

        Log.e(
            "onStart",
            "onStart: FourthActivity terlihat di layar"
        )
    }

    // =========================
    // ON DESTROY
    // =========================

    override fun onDestroy() {
        super.onDestroy()

        Log.e(
            "onDestroy",
            "FourthActivity dihapus dari stack"
        )
    }
}