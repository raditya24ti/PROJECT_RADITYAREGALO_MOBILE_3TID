package com.example.rrf_lays.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object Format {
    fun Int.toRupiah(): String {
        val formatter = DecimalFormat("#,###", DecimalFormatSymbols(Locale("id", "ID")))
        return "Rp " + formatter.format(this)
    }
}
