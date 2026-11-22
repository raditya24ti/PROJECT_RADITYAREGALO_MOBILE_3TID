package com.example.rrf_lays.admin.outlet

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rrf_lays.R
import com.example.rrf_lays.data.model.Outlet

class OutletManagementAdapter(
    private val listOutlet: List<Outlet>,
    private val onEdit: (Outlet) -> Unit,
    private val onDelete: (Outlet) -> Unit
) : RecyclerView.Adapter<OutletManagementAdapter.OutletViewHolder>() {

    class OutletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvOutletNama)
        val tvAlamat: TextView = view.findViewById(R.id.tvOutletAlamat)
        val chipJamBuka: TextView = view.findViewById(R.id.chipJamBuka)
        val btnLihatPeta: Button = view.findViewById(R.id.btnLihatPeta)
        val btnEdit: ImageButton = view.findViewById(R.id.btnOutletEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnOutletDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutletViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outlet_management, parent, false)
        return OutletViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutletViewHolder, position: Int) {
        val outlet = listOutlet[position]
        holder.tvNama.text = outlet.nama
        holder.tvAlamat.text = outlet.alamat
        holder.chipJamBuka.text = outlet.jam_buka ?: "08.00 - 21.00"

        holder.btnLihatPeta.setOnClickListener {
            val context = holder.itemView.context
            val lat = outlet.latitude
            val lng = outlet.longitude
            val nama = outlet.nama
            val uri = Uri.parse("geo:0,0?q=$lat,$lng($nama)")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }
        }

        holder.btnEdit.setOnClickListener {
            onEdit(outlet)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(outlet)
        }
    }

    override fun getItemCount(): Int = listOutlet.size
}
