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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rrf_lays.data.api.ProdukApiService
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.data.model.Produk
import com.example.rrf_lays.databinding.FragmentKatalogBinding
import kotlinx.coroutines.launch

class KatalogFragment : Fragment() {

    private var _binding: FragmentKatalogBinding? = null
    private val binding get() = _binding!!
    private val produkApi by lazy { SupabaseClient.create<ProdukApiService>() }

    private var allProdukList: List<Produk> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchKatalogProduk()

        // FITUR PENCARIAN KATALOG DINAMIS
        binding.etSearchKatalog.addTextChangedListener { text ->
            val query = text.toString().trim().lowercase()
            val filtered = allProdukList.filter {
                it.nama.lowercase().contains(query) || (it.deskripsi?.lowercase()?.contains(query) == true)
            }
            setupRecyclerView(filtered)
        }
    }

    private fun fetchKatalogProduk() {
        lifecycleScope.launch {
            try {
                val response = produkApi.getAll(order = "created_at.desc")
                if (response.isSuccessful) {
                    allProdukList = response.body() ?: emptyList()
                    setupRecyclerView(allProdukList)
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat katalog", Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat katalog", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(list: List<Produk>) {
        val produkAdapter = ProdukAdapter(list) { produk ->
            val defaultShopee = "https://shopee.co.id/Rengginang-Sabit-Pekanbaru-i.974795943.22469073604"
            val link = if (!produk.link_shopee.isNullOrBlank()) produk.link_shopee else defaultShopee
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        }
        binding.rvKatalogProduk.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = produkAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
