package com.example.rrf_lays

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rrf_lays.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // Setup View Binding untuk Fragment
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ==========================================
        // FITUR 1: IMAGE SLIDER (BANNER PROMO)
        // ==========================================

        // 1. Siapkan daftar gambar dari drawable
        val imageList = listOf(
            R.drawable.original,
            R.drawable.cumi,
            R.drawable.bawang
        )

        // 2. Hubungkan daftar gambar ke BannerAdapter, lalu pasang ke ViewPager2
        val bannerAdapter = BannerAdapter(imageList)
        binding.viewPagerBanner.adapter = bannerAdapter

        // 3. Tambahkan efek animasi pergeseran (Page Transformer)
        binding.viewPagerBanner.setPageTransformer { page, position ->
            page.translationX = -position * (view.width * 0.1f)
        }


        // ==========================================
        // FITUR 2: RECYCLERVIEW GRID
        // ==========================================

        val daftarProduk = listOf(
            ProdukModel("Rengginang Original", "250g", "Rp25.000", R.drawable.original),
            ProdukModel("Rengginang Rawit", "250g", "Rp27.000", R.drawable.rawit),
            ProdukModel("Rengginang Cumi", "250g", "Rp28.000", R.drawable.cumi),
            ProdukModel("Rengginang Bawang", "250g", "Rp26.000", R.drawable.bawang),
            ProdukModel("Rengginang Udang", "250g", "Rp29.000", R.drawable.udang)
        )

        val produkAdapter = ProdukAdapter(daftarProduk) {
            // Aksi saat produk diklik: Buka Shopee
            val linkShopee = "https://shopee.co.id/Rengginang-Sabit-Pekanbaru-i.974795943.22469073604"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkShopee)))
        }

        binding.rvProdukUnggulan.apply {
            // Menerapkan tampilan Grid 2 Kolom
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = produkAdapter
        }


        // ==========================================
        // FITUR 3: ONBOARD SCREEN (TOMBOL INFO)
        // ==========================================
        binding.btnInfo.setOnClickListener {
            // Memanggil Activity Tutorial yang berada di package tutorial
            val intent = Intent(requireContext(), com.example.rrf_lays.tutorial.TutorialMessageActivity::class.java)
            startActivity(intent)
        }


        // ==========================================
        // FITUR LAMA: NAVIGASI KE HALAMAN ADMIN
        // ==========================================
        binding.btnAdmin.setOnClickListener {
            // Menerapkan requireContext() sesuai instruksi modul
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Mencegah memory leak
    }
}