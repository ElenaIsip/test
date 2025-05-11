package com.example.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServicesAdapter(
    private var services: List<Service>,
    private val onItemClick: (Service) -> Unit
) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.serviceTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = services[position]
        holder.title.text = service.title
        holder.itemView.setOnClickListener { onItemClick(service) }
    }

    override fun getItemCount() = services.size

    fun filterList(filteredList: List<Service>) {
        services = filteredList
        notifyDataSetChanged()
    }
}