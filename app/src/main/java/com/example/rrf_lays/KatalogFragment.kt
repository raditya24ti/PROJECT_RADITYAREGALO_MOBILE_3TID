package com.example.rrf_lays

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rrf_lays.databinding.FragmentKatalogBinding

class KatalogFragment : Fragment() {

    // Setup ViewBinding khusus untuk Fragment
    private var _binding: FragmentKatalogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FITUR 1: Pesan via WhatsApp (Direct Order)
        // PERHATIAN: Pastikan ID tombol di fragment_katalog.xml kamu adalah "btnBeliCumi"
        // Jika beda, ganti tulisan btnBeliCumi di bawah sesuai ID aslinya.
        binding.btnBeliCumi.setOnClickListener {

            val linkShopee = "https://share.google/iWFwJHJnMRwaOj7Bk"
            val uri = Uri.parse(linkShopee)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Mencegah memory leak saat Fragment dihancurkan
        _binding = null
    }
}