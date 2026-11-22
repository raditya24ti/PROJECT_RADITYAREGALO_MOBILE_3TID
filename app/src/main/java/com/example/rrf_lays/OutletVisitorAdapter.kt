package com.example.rrf_lays

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rrf_lays.data.model.Outlet

class OutletVisitorAdapter(
    private val listOutlet: List<Outlet>,
    private val onRuteClick: (Outlet) -> Unit
) : RecyclerView.Adapter<OutletVisitorAdapter.OutletViewHolder>() {

    class OutletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvVisitorOutletNama)
        val tvAlamat: TextView = view.findViewById(R.id.tvVisitorOutletAlamat)
        val tvJamBuka: TextView = view.findViewById(R.id.tvVisitorOutletJam)
        val btnRute: Button = view.findViewById(R.id.btnVisitorRute)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutletViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_visitor_outlet, parent, false)
        return OutletViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutletViewHolder, position: Int) {
        val outlet = listOutlet[position]
        holder.tvNama.text = outlet.nama
        holder.tvAlamat.text = outlet.alamat
        holder.tvJamBuka.text = "Jam Buka: ${outlet.jam_buka ?: "08.00 - 21.00"}"

        holder.btnRute.setOnClickListener {
            onRuteClick(outlet)
        }
    }

    override fun getItemCount(): Int = listOutlet.size
}
