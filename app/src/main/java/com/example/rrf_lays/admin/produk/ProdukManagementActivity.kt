package com.example.rrf_lays.admin.produk

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rrf_lays.AdminNavHelper
import com.example.rrf_lays.R
import com.example.rrf_lays.data.api.ApiErrorHelper
import com.example.rrf_lays.data.api.ProdukApiService
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.model.Produk
import com.example.rrf_lays.databinding.ActivityProdukManagementBinding
import com.example.rrf_lays.util.ImageLoader.loadGambar
import kotlinx.coroutines.launch

class ProdukManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdukManagementBinding
    private val produkApi by lazy { SupabaseClient.create<ProdukApiService>() }

    private var editingProdukId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdukManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation Drawer & Top Bar
        AdminNavHelper.setupAdminNavigation(this, binding.drawerLayout, binding.navView, R.id.nav_katalog)

        // Live preview image when text changes
        binding.inputGambar.addTextChangedListener { text ->
            binding.imgPreview.loadGambar(text.toString().trim())
        }

        // SwipeRefresh
        binding.swipeRefresh.setOnRefreshListener {
            loadProdukList()
        }

        // Publish / Simpan Perubahan Button
        binding.btnPublish.setOnClickListener {
            submitForm()
        }

        // Batal Edit
        binding.btnBatalEdit.setOnClickListener {
            resetFormToCreateMode()
        }

        loadProdukList()
    }

    private fun loadProdukList() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launch {
            try {
                val response = produkApi.getAll(order = "created_at.desc")
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    setupRecyclerView(list)
                } else {
                    Toast.makeText(this@ProdukManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProdukManagementActivity, "Gagal memuat produk: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView(list: List<Produk>) {
        binding.tvTotalProdukCount.text = "Total produk: ${list.size}"

        if (list.isEmpty()) {
            binding.rvProduk.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvProduk.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE

            val adapter = ProdukManagementAdapter(
                listProduk = list,
                onEdit = { produk ->
                    enterEditMode(produk)
                },
                onDelete = { produk ->
                    confirmDelete(produk)
                }
            )
            binding.rvProduk.layoutManager = LinearLayoutManager(this)
            binding.rvProduk.adapter = adapter
        }
    }

    private fun enterEditMode(produk: Produk) {
        editingProdukId = produk.id
        binding.tvFormTitle.text = "Edit Produk"
        binding.btnPublish.text = "Simpan Perubahan"
        binding.btnBatalEdit.visibility = View.VISIBLE

        binding.inputNama.setText(produk.nama)
        binding.inputDeskripsi.setText(produk.deskripsi ?: "")
        binding.inputHarga.setText(produk.harga.toString())
        binding.inputBerat.setText(produk.berat)
        binding.inputGambar.setText(produk.gambar ?: "")
        binding.inputLinkShopee.setText(produk.link_shopee ?: "")

        binding.nestedScrollView.smoothScrollTo(0, 0)
    }

    private fun resetFormToCreateMode() {
        editingProdukId = null
        binding.tvFormTitle.text = "Entri Produk Baru"
        binding.btnPublish.text = "Publish Produk Baru"
        binding.btnBatalEdit.visibility = View.GONE

        binding.inputNama.text?.clear()
        binding.inputDeskripsi.text?.clear()
        binding.inputHarga.text?.clear()
        binding.inputBerat.text?.clear()
        binding.inputGambar.text?.clear()
        binding.inputLinkShopee.text?.clear()

        binding.tilNama.error = null
        binding.tilHarga.error = null
        binding.tilBerat.error = null
    }

    private fun submitForm() {
        val nama = binding.inputNama.text.toString().trim()
        val deskripsi = binding.inputDeskripsi.text.toString().trim()
        val hargaStr = binding.inputHarga.text.toString().trim()
        val beratInput = binding.inputBerat.text.toString().trim()
        val berat = if (beratInput.isEmpty()) "250g" else beratInput
        val gambar = binding.inputGambar.text.toString().trim()
        val linkShopee = binding.inputLinkShopee.text.toString().trim()

        // Validasi
        var isValid = true
        if (nama.isEmpty()) {
            binding.tilNama.error = "Nama produk wajib diisi"
            isValid = false
        } else {
            binding.tilNama.error = null
        }

        val harga = hargaStr.toIntOrNull()
        if (harga == null || harga < 0) {
            binding.tilHarga.error = "Harga wajib angka >= 0"
            isValid = false
        } else {
            binding.tilHarga.error = null
        }

        if (!isValid) return

        binding.btnPublish.isEnabled = false
        binding.btnPublish.text = "Menyimpan..."

        val produkObj = Produk(
            id = editingProdukId,
            nama = nama,
            berat = berat,
            harga = harga!!,
            deskripsi = deskripsi.ifEmpty { null },
            gambar = gambar.ifEmpty { null },
            link_shopee = linkShopee.ifEmpty { null },
            created_at = null
        )

        lifecycleScope.launch {
            try {
                if (editingProdukId == null) {
                    // Create (POST)
                    val response = produkApi.create(produkObj)
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProdukManagementActivity, "Produk berhasil dipublish", Toast.LENGTH_SHORT).show()
                        resetFormToCreateMode()
                        loadProdukList()
                    } else {
                        Toast.makeText(this@ProdukManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Update (PATCH)
                    val response = produkApi.update("eq.$editingProdukId", produkObj)
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProdukManagementActivity, "Perubahan disimpan", Toast.LENGTH_SHORT).show()
                        resetFormToCreateMode()
                        loadProdukList()
                    } else {
                        Toast.makeText(this@ProdukManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProdukManagementActivity, "Gagal menyimpan: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnPublish.isEnabled = true
                binding.btnPublish.text = if (editingProdukId == null) "Publish Produk Baru" else "Simpan Perubahan"
            }
        }
    }

    private fun confirmDelete(produk: Produk) {
        AlertDialog.Builder(this)
            .setTitle("Hapus produk?")
            .setMessage("${produk.nama} akan dihapus permanen.")
            .setPositiveButton("Hapus") { dialog, _ ->
                dialog.dismiss()
                deleteProduk(produk.id)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteProduk(id: Long?) {
        if (id == null) return
        lifecycleScope.launch {
            try {
                val response = produkApi.delete("eq.$id")
                if (response.isSuccessful) {
                    Toast.makeText(this@ProdukManagementActivity, "Produk dihapus", Toast.LENGTH_SHORT).show()
                    loadProdukList()
                } else {
                    Toast.makeText(this@ProdukManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProdukManagementActivity, "Gagal menghapus: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
