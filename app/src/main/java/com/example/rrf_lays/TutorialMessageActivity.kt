package com.example.rrf_lays.tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.databinding.ActivityTutorialMessageBinding

class TutorialMessageActivity : AppCompatActivity() {

    // Setup ViewBinding
    private lateinit var binding: ActivityTutorialMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Siapkan daftar Fragment (Slide 1, 2, dan 3)
        val fragmentsList = listOf(
            Tutorial1Fragment(),
            Tutorial2Fragment(),
            Tutorial3Fragment()
        )

        // 2. Hubungkan daftar Fragment ke Adapter
        val adapter = TutorialFragmentAdapter(this, fragmentsList)

        // 3. Pasang adapter ke ViewPager2
        // PERHATIAN: Pastikan ID ViewPager2 di file XML kamu adalah "tutorialMessageViewPager"
        binding.tutorialMessageViewPager.adapter = adapter

        // 4. Hubungkan DotsIndicator (titik-titik di bawah) dengan ViewPager2
        // PERHATIAN: Pastikan ID DotsIndicator di file XML kamu adalah "dotIndicator"
        binding.dotIndicator.attachTo(binding.tutorialMessageViewPager)
    }
}