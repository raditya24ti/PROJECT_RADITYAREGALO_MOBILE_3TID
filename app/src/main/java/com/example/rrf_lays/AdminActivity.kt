package com.example.rrf_lays

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rrf_lays.admin.outlet.OutletManagementActivity
import com.example.rrf_lays.admin.produk.ProdukManagementActivity
import com.example.rrf_lays.data.api.ApiErrorHelper
import com.example.rrf_lays.data.api.OutletApiService
import com.example.rrf_lays.data.api.ProdukApiService
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.api.UserApiService
import com.example.rrf_lays.data.model.Produk
import com.example.rrf_lays.databinding.ActivityAdminBinding
import com.example.rrf_lays.util.Format.toRupiah
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val produkApi by lazy { SupabaseClient.create<ProdukApiService>() }
    private val outletApi by lazy { SupabaseClient.create<OutletApiService>() }
    private val userApi by lazy { SupabaseClient.create<UserApiService>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Admin Navigation (Drawer + Top Bar + More Menu)
        AdminNavHelper.setupAdminNavigation(this, binding.drawerLayout, binding.navView, R.id.nav_dashboard)

        // Shortcut row click listeners
        binding.rowShortcutKatalog.setOnClickListener {
            val intent = Intent(this, ProdukManagementActivity::class.java)
            startActivity(intent)
        }
        binding.rowShortcutOutlet.setOnClickListener {
            val intent = Intent(this, OutletManagementActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        lifecycleScope.launch {
            try {
                // Fetch Produk
                val produkResponse = produkApi.getAll(order = "created_at.desc")
                // Fetch Outlet
                val outletResponse = outletApi.getAll()
                // Fetch Users Count
                val userResponse = userApi.count()

                if (produkResponse.isSuccessful && outletResponse.isSuccessful && userResponse.isSuccessful) {
                    val produkList = produkResponse.body() ?: emptyList()
                    val outletList = outletResponse.body() ?: emptyList()
                    val userList = userResponse.body() ?: emptyList()

                    val totalProduk = produkList.size
                    val totalOutlet = outletList.size
                    val totalUser = userList.size

                    // Update UI Stats
                    binding.tvTotalProdukVal.text = totalProduk.toString()
                    binding.chipProdukCount.text = "● $totalProduk Menu"
                    binding.tvBoxProduk.text = "$totalProduk item"

                    binding.tvTotalOutletVal.text = totalOutlet.toString()
                    binding.chipOutletCount.text = "● $totalOutlet Lokasi"
                    binding.tvBoxOutlet.text = "$totalOutlet lokasi"

                    binding.tvBoxUser.text = "$totalUser akun"

                    // Average price
                    val avgHarga = if (produkList.isNotEmpty()) {
                        produkList.map { it.harga }.average().toInt()
                    } else {
                        0
                    }
                    binding.tvAvgHargaVal.text = avgHarga.toRupiah()

                    // Top 5 newest products
                    val top5 = produkList.take(5)
                    populateTop5Products(top5)

                } else {
                    val err = when {
                        !produkResponse.isSuccessful -> ApiErrorHelper.parseError(produkResponse)
                        !outletResponse.isSuccessful -> ApiErrorHelper.parseError(outletResponse)
                        else -> ApiErrorHelper.parseError(userResponse)
                    }
                    Toast.makeText(this@AdminActivity, err, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Gagal memuat data: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun populateTop5Products(list: List<Produk>) {
        binding.containerTop5Produk.removeAllViews()

        for (produk in list) {
            val rowView = layoutInflater.inflate(R.layout.item_admin_produk_row, binding.containerTop5Produk, false)

            val tvNama = rowView.findViewById<TextView>(R.id.tvRowNama)
            val tvBerat = rowView.findViewById<TextView>(R.id.tvRowBerat)
            val tvHarga = rowView.findViewById<TextView>(R.id.tvRowHarga)

            tvNama.text = produk.nama
            tvBerat.text = produk.berat
            tvHarga.text = produk.harga.toRupiah()

            binding.containerTop5Produk.addView(rowView)

            // Add divider between rows
            val divider = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
                setBackgroundColor(resources.getColor(R.color.admin_border, null))
            }
            binding.containerTop5Produk.addView(divider)
        }
    }
}
