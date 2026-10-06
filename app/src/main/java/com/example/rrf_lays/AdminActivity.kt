package com.example.rrf_lays

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Admin Rengginang Sabit"
        }
    }

    // Menu ☰
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu, menu)
        return true
    }

    // Aksi menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            // Profil Developer
            R.id.action_profile -> {

                val intent = Intent(
                    this,
                    DeveloperProfileActivity::class.java
                )

                startActivity(intent)

                true
            }

            // Logout
            R.id.action_logout -> {

                showLogoutDialog()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Dialog Logout
    private fun showLogoutDialog() {

        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { dialog, _ ->

                // Hapus data login
                val editor = sharedPref.edit()

                editor.clear()
                editor.apply()

                dialog.dismiss()

                // Logout → kembali ke MainActivity
                val intent = Intent(
                    this,
                    MainActivity::class.java
                )

                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(intent)

                finish()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}