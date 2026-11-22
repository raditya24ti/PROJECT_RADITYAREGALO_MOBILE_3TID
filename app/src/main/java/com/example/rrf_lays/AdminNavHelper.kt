package com.example.rrf_lays

import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.rrf_lays.admin.outlet.OutletManagementActivity
import com.example.rrf_lays.admin.produk.ProdukManagementActivity
import com.google.android.material.navigation.NavigationView

object AdminNavHelper {
    fun setupAdminNavigation(
        activity: AppCompatActivity,
        drawerLayout: DrawerLayout,
        navigationView: NavigationView,
        currentMenuItemId: Int
    ) {
        val btnNavMenu = activity.findViewById<ImageButton>(R.id.btnNavMenu)
        val btnNavMore = activity.findViewById<ImageButton>(R.id.btnNavMore)

        btnNavMenu?.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        btnNavMore?.setOnClickListener { view ->
            val popup = PopupMenu(activity, view)
            popup.menuInflater.inflate(R.menu.admin_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_profile -> {
                        activity.startActivity(Intent(activity, DeveloperProfileActivity::class.java))
                        true
                    }
                    R.id.action_logout -> {
                        showLogoutDialog(activity)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        navigationView.setCheckedItem(currentMenuItemId)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    if (activity !is AdminActivity) {
                        val intent = Intent(activity, AdminActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity.startActivity(intent)
                    }
                    true
                }
                R.id.nav_katalog -> {
                    if (activity !is ProdukManagementActivity) {
                        val intent = Intent(activity, ProdukManagementActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity.startActivity(intent)
                    }
                    true
                }
                R.id.nav_outlet -> {
                    if (activity !is OutletManagementActivity) {
                        val intent = Intent(activity, OutletManagementActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        activity.startActivity(intent)
                    }
                    true
                }
                R.id.nav_lihat_aplikasi -> {
                    val intent = Intent(activity, BaseActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                    activity.finish()
                    true
                }
                R.id.nav_profile -> {
                    activity.startActivity(Intent(activity, DeveloperProfileActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    showLogoutDialog(activity)
                    true
                }
                else -> false
            }
        }
    }

    private fun showLogoutDialog(activity: AppCompatActivity) {
        val sharedPref = activity.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        AlertDialog.Builder(activity)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { dialog, _ ->
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()
                dialog.dismiss()

                val intent = Intent(activity, BaseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(intent)
                activity.finish()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
