package com.example.rrf_lays.admin.produk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rrf_lays.R
import com.example.rrf_lays.data.model.Produk
import com.example.rrf_lays.util.Format.toRupiah
import com.example.rrf_lays.util.ImageLoader.loadGambar

class ProdukManagementAdapter(
    private val listProduk: List<Produk>,
    private val onEdit: (Produk) -> Unit,
    private val onDelete: (Produk) -> Unit
) : RecyclerView.Adapter<ProdukManagementAdapter.ProdukViewHolder>() {

    class ProdukViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduk: ImageView = view.findViewById(R.id.imgItemProduk)
        val tvNama: TextView = view.findViewById(R.id.tvItemNama)
        val tvBerat: TextView = view.findViewById(R.id.tvItemBerat)
        val tvHarga: TextView = view.findViewById(R.id.tvItemHarga)
        val btnEdit: ImageButton = view.findViewById(R.id.btnItemEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnItemDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produk_management, parent, false)
        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val produk = listProduk[position]
        holder.tvNama.text = produk.nama
        holder.tvBerat.text = produk.berat
        holder.tvHarga.text = produk.harga.toRupiah()
        holder.imgProduk.loadGambar(produk.gambar)

        holder.btnEdit.setOnClickListener {
            onEdit(produk)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(produk)
        }
    }

    override fun getItemCount(): Int = listProduk.size
}
