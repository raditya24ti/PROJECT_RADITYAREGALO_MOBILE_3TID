package com.example.rrf_lays

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        // FITUR BARU: IMAGE SLIDER (BANNER PROMO)
        // ==========================================

        // 1. Siapkan daftar gambar dari drawable (kamu bisa ganti dengan gambar banner asli nanti)
        val imageList = listOf(
            R.drawable.original,
            R.drawable.cumi,
            R.drawable.bawang
        )

        // 2. Hubungkan daftar gambar ke BannerAdapter, lalu pasang ke ViewPager2
        val bannerAdapter = BannerAdapter(imageList)
        binding.viewPagerBanner.adapter = bannerAdapter

        // 3. Tambahkan efek animasi pergeseran (Page Transformer) agar transisinya mulus
        binding.viewPagerBanner.setPageTransformer { page, position ->
            page.translationX = -position * (view.width * 0.1f)
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