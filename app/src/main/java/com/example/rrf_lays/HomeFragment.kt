package com.example.rrf_lays

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rrf_lays.data.api.ProdukApiService
import com.example.rrf_lays.data.api.SupabaseClient
import com.example.rrf_lays.databinding.FragmentHomeBinding
import com.example.rrf_lays.tutorial.TutorialMessageActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val produkApi by lazy { SupabaseClient.create<ProdukApiService>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Banner Slider
        val imageList = listOf(
            R.drawable.original,
            R.drawable.cumi,
            R.drawable.bawang
        )
        val bannerAdapter = BannerAdapter(imageList)
        binding.viewPagerBanner.adapter = bannerAdapter
        binding.viewPagerBanner.setPageTransformer { page, position ->
            page.translationX = -position * (view.width * 0.1f)
        }

        // Fetch Produk from Supabase
        fetchProdukUnggulan()

        // Navigasi ke Katalog
        val navigateToKatalog = View.OnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
            bottomNav?.selectedItemId = R.id.katalog
        }
        binding.btnMasuk.setOnClickListener(navigateToKatalog)
        binding.tvLihatSemua.setOnClickListener(navigateToKatalog)

        // Tutorial
        binding.btnInfo.setOnClickListener {
            val intent = Intent(requireContext(), TutorialMessageActivity::class.java)
            startActivity(intent)
        }

        // Admin
        binding.btnAdmin.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProdukUnggulan() {
        lifecycleScope.launch {
            try {
                val response = produkApi.getAll()
                if (response.isSuccessful) {
                    val listProduk = response.body() ?: emptyList()
                    val produkAdapter = ProdukAdapter(listProduk) { produk ->
                        val defaultShopee = "https://shopee.co.id/Rengginang-Sabit-Pekanbaru-i.974795943.22469073604"
                        val link = if (!produk.link_shopee.isNullOrBlank()) produk.link_shopee else defaultShopee
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }
                    binding.rvProdukUnggulan.apply {
                        layoutManager = GridLayoutManager(requireContext(), 2)
                        adapter = produkAdapter
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat produk", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat produk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
