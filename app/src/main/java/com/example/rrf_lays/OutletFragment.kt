package com.example.rrf_lays

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rrf_lays.databinding.FragmentOutletBinding

class OutletFragment : Fragment() {

    private var _binding: FragmentOutletBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Klik Outlet Pusat (Diarahkan ke koordinat PCR / Rumbai)
        binding.btnRuteOutletPusat.setOnClickListener {
            bukaPeta("0.5746761", "101.425816", "Outlet Pusat Rengginang Sabit")
        }

        // 2. Klik Outlet Sudirman (Diarahkan ke koordinat Jl. Sudirman Pekanbaru)
        binding.btnRuteOutletSudirman.setOnClickListener {
            bukaPeta("0.500411", "101.451874", "Outlet Sudirman Rengginang Sabit")
        }
    }

    // Fungsi utama untuk memicu Implicit Intent ke Google Maps
    private fun bukaPeta(latitude: String, longitude: String, namaLokasi: String) {
        // Skema geo: bawaan Android
        val uri = Uri.parse("geo:0,0?q=$latitude,$longitude($namaLokasi)")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // Cadangan jika user tidak punya aplikasi Maps
            val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
            startActivity(Intent(Intent.ACTION_VIEW, browserUri))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}