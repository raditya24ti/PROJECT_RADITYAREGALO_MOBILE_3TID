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

    // DISINI KITA MENERAPKAN ILMU DARI MODUL: onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Asumsi ID tombol Admin di layout XML kamu adalah btn_admin
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