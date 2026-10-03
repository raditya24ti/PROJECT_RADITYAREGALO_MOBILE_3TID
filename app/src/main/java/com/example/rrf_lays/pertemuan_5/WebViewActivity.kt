package com.example.rrf_lays.pertemuan_5

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.rrf_lays.R
import com.example.rrf_lays.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar - Pertemuan 5
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Web Merdeka"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // WebView - Pertemuan 5
        binding.webView.webViewClient = WebViewClient()

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.loadUrl("https://merdeka.com")

        // Back Android - Pertemuan 5
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )

        // AppBar collapse / expand saat WebView di-scroll
        binding.webView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->

            if (scrollY > oldScrollY) {
                binding.appBar.setExpanded(false, true)

            } else if (scrollY < oldScrollY) {
                binding.appBar.setExpanded(true, true)
            }
        }
    }

    // Toolbar Back - Pertemuan 5
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}