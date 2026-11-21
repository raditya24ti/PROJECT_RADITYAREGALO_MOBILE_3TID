package com.example.rrf_lays

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rrf_lays.data.api.OutletApiService
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.model.Outlet
import com.example.rrf_lays.databinding.FragmentOutletBinding
import kotlinx.coroutines.launch

class OutletFragment : Fragment() {

    private var _binding: FragmentOutletBinding? = null
    private val binding get() = _binding!!
    private val outletApi by lazy { SupabaseClient.create<OutletApiService>() }

    private var allOutlets: List<Outlet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchOutlets()

        // FITUR PENCARIAN OUTLET
        binding.etSearchOutlet.addTextChangedListener { text ->
            val query = text.toString().trim().lowercase()
            val filtered = allOutlets.filter {
                it.nama.lowercase().contains(query) || it.alamat.lowercase().contains(query)
            }
            setupRecyclerView(filtered)
        }
    }

    private fun fetchOutlets() {
        lifecycleScope.launch {
            try {
                val response = outletApi.getAll()
                if (response.isSuccessful) {
                    allOutlets = response.body() ?: emptyList()
                    setupRecyclerView(allOutlets)
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat outlet", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat outlet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(list: List<Outlet>) {
        val adapter = OutletVisitorAdapter(list) { outlet ->
            bukaPeta(outlet.latitude.toString(), outlet.longitude.toString(), outlet.nama)
        }
        binding.rvOutlet.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOutlet.adapter = adapter
    }

    private fun bukaPeta(latitude: String, longitude: String, namaLokasi: String) {
        val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude($namaLokasi)")
        val intent = Intent(Intent.ACTION_VIEW, browserUri)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
