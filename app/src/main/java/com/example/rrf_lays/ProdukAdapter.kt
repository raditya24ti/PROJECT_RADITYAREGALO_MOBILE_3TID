package com.example.rrf_lays

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdukAdapter(
    private val listProduk: List<ProdukModel>,
    private val onClickPesan: (ProdukModel) -> Unit
) : RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>() {

    class ProdukViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduk: ImageView = view.findViewById(R.id.imgGridProduk)
        val tvNama: TextView = view.findViewById(R.id.tvGridNama)
        val tvBerat: TextView = view.findViewById(R.id.tvGridBerat)
        val tvHarga: TextView = view.findViewById(R.id.tvGridHarga)
        val btnPesan: Button = view.findViewById(R.id.btnGridPesan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produk_grid, parent, false)
        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val produk = listProduk[position]
        holder.tvNama.text = produk.nama
        holder.tvBerat.text = produk.berat
        holder.tvHarga.text = produk.harga
        holder.imgProduk.setImageResource(produk.gambarResId)

        holder.btnPesan.setOnClickListener {
            onClickPesan(produk)
        }
    }

    override fun getItemCount(): Int = listProduk.size
}