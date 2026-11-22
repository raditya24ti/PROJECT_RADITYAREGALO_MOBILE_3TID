package com.example.rrf_lays.admin.outlet

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rrf_lays.AdminNavHelper
import com.example.rrf_lays.R
import com.example.rrf_lays.data.api.ApiErrorHelper
import com.example.rrf_lays.data.api.OutletApiService
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.model.Outlet
import com.example.rrf_lays.databinding.ActivityOutletManagementBinding
import kotlinx.coroutines.launch

class OutletManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutletManagementBinding
    private val outletApi by lazy { SupabaseClient.create<OutletApiService>() }

    private var editingOutletId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutletManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Drawer & Top Bar
        AdminNavHelper.setupAdminNavigation(this, binding.drawerLayout, binding.navView, R.id.nav_outlet)

        // SwipeRefresh
        binding.swipeRefresh.setOnRefreshListener {
            loadOutletList()
        }

        // Paste koordinat button
        binding.btnPasteKoordinat.setOnClickListener {
            pasteKoordinatFromClipboard()
        }

        // Simpan / Publish button
        binding.btnSimpanOutlet.setOnClickListener {
            submitForm()
        }

        // Batal Edit
        binding.btnBatalEdit.setOnClickListener {
            resetFormToCreateMode()
        }

        loadOutletList()
    }

    private fun pasteKoordinatFromClipboard() {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboard.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val pasteText = clipData.getItemAt(0).text?.toString()?.trim() ?: ""
            val parts = pasteText.split(Regex("[,\\s]+"))
            if (parts.size >= 2) {
                val lat = parts[0].toDoubleOrNull()
                val lng = parts[1].toDoubleOrNull()
                if (lat != null && lng != null) {
                    binding.inputLatitude.setText(lat.toString())
                    binding.inputLongitude.setText(lng.toString())
                    Toast.makeText(this, "Koordinat berhasil ditempel", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            Toast.makeText(this, "Format clipboard tidak valid (harus lat, lng)", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Clipboard kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadOutletList() {
        binding.swipeRefresh.isRefreshing = true
        lifecycleScope.launch {
            try {
                val response = outletApi.getAll(order = "created_at.desc")
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    setupRecyclerView(list)
                } else {
                    Toast.makeText(this@OutletManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OutletManagementActivity, "Gagal memuat outlet: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView(list: List<Outlet>) {
        binding.tvTotalOutletCount.text = "Total outlet aktif saat ini: ${list.size}"

        if (list.isEmpty()) {
            binding.rvOutlet.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvOutlet.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE

            val adapter = OutletManagementAdapter(
                listOutlet = list,
                onEdit = { outlet ->
                    enterEditMode(outlet)
                },
                onDelete = { outlet ->
                    confirmDelete(outlet)
                }
            )
            binding.rvOutlet.layoutManager = LinearLayoutManager(this)
            binding.rvOutlet.adapter = adapter
        }
    }

    private fun enterEditMode(outlet: Outlet) {
        editingOutletId = outlet.id
        binding.tvFormTitle.text = "Edit Outlet"
        binding.btnSimpanOutlet.text = "Simpan Perubahan"
        binding.btnBatalEdit.visibility = View.VISIBLE

        binding.inputNamaOutlet.setText(outlet.nama)
        binding.inputAlamat.setText(outlet.alamat)
        binding.inputLatitude.setText(outlet.latitude.toString())
        binding.inputLongitude.setText(outlet.longitude.toString())
        binding.inputJamBuka.setText(outlet.jam_buka ?: "")
        binding.inputNoTelp.setText(outlet.no_telp ?: "")

        binding.nestedScrollView.smoothScrollTo(0, 0)
    }

    private fun resetFormToCreateMode() {
        editingOutletId = null
        binding.tvFormTitle.text = "Tambah Outlet"
        binding.btnSimpanOutlet.text = "+  Simpan Outlet"
        binding.btnBatalEdit.visibility = View.GONE

        binding.inputNamaOutlet.text?.clear()
        binding.inputAlamat.text?.clear()
        binding.inputLatitude.text?.clear()
        binding.inputLongitude.text?.clear()
        binding.inputJamBuka.text?.clear()
        binding.inputNoTelp.text?.clear()

        binding.tilNamaOutlet.error = null
        binding.tilAlamat.error = null
        binding.tilLatitude.error = null
        binding.tilLongitude.error = null
    }

    private fun submitForm() {
        val nama = binding.inputNamaOutlet.text.toString().trim()
        val alamat = binding.inputAlamat.text.toString().trim()
        val latStr = binding.inputLatitude.text.toString().trim()
        val lngStr = binding.inputLongitude.text.toString().trim()
        val jamBuka = binding.inputJamBuka.text.toString().trim()
        val noTelp = binding.inputNoTelp.text.toString().trim()

        var isValid = true
        if (nama.isEmpty()) {
            binding.tilNamaOutlet.error = "Nama outlet wajib diisi"
            isValid = false
        } else {
            binding.tilNamaOutlet.error = null
        }

        if (alamat.isEmpty()) {
            binding.tilAlamat.error = "Alamat outlet wajib diisi"
            isValid = false
        } else {
            binding.tilAlamat.error = null
        }

        val lat = latStr.toDoubleOrNull()
        if (lat == null || lat < -90.0 || lat > 90.0) {
            binding.tilLatitude.error = "Latitude wajib antara -90 dan 90"
            isValid = false
        } else {
            binding.tilLatitude.error = null
        }

        val lng = lngStr.toDoubleOrNull()
        if (lng == null || lng < -180.0 || lng > 180.0) {
            binding.tilLongitude.error = "Longitude wajib antara -180 dan 180"
            isValid = false
        } else {
            binding.tilLongitude.error = null
        }

        if (!isValid) return

        binding.btnSimpanOutlet.isEnabled = false
        binding.btnSimpanOutlet.text = "Menyimpan..."

        val outletObj = Outlet(
            id = editingOutletId,
            nama = nama,
            alamat = alamat,
            latitude = lat!!,
            longitude = lng!!,
            jam_buka = jamBuka.ifEmpty { null },
            no_telp = noTelp.ifEmpty { null },
            created_at = null
        )

        lifecycleScope.launch {
            try {
                if (editingOutletId == null) {
                    // Create (POST)
                    val response = outletApi.create(outletObj)
                    if (response.isSuccessful) {
                        Toast.makeText(this@OutletManagementActivity, "Outlet berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        resetFormToCreateMode()
                        loadOutletList()
                    } else {
                        Toast.makeText(this@OutletManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Update (PATCH)
                    val response = outletApi.update("eq.$editingOutletId", outletObj)
                    if (response.isSuccessful) {
                        Toast.makeText(this@OutletManagementActivity, "Perubahan outlet disimpan", Toast.LENGTH_SHORT).show()
                        resetFormToCreateMode()
                        loadOutletList()
                    } else {
                        Toast.makeText(this@OutletManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@OutletManagementActivity, "Gagal menyimpan: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnSimpanOutlet.isEnabled = true
                binding.btnSimpanOutlet.text = if (editingOutletId == null) "+  Simpan Outlet" else "Simpan Perubahan"
            }
        }
    }

    private fun confirmDelete(outlet: Outlet) {
        AlertDialog.Builder(this)
            .setTitle("Hapus outlet?")
            .setMessage("${outlet.nama} akan dihapus permanen.")
            .setPositiveButton("Hapus") { dialog, _ ->
                dialog.dismiss()
                deleteOutlet(outlet.id)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteOutlet(id: Long?) {
        if (id == null) return
        lifecycleScope.launch {
            try {
                val response = outletApi.delete("eq.$id")
                if (response.isSuccessful) {
                    Toast.makeText(this@OutletManagementActivity, "Outlet dihapus", Toast.LENGTH_SHORT).show()
                    loadOutletList()
                } else {
                    Toast.makeText(this@OutletManagementActivity, ApiErrorHelper.parseError(response), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OutletManagementActivity, "Gagal menghapus: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
