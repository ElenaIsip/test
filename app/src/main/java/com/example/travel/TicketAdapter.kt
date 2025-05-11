package com.example.travel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TicketAdapter(
    context: Context,
    private val tickets: List<Ticket>
) : ArrayAdapter<Ticket>(context, 0, tickets) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val ticket = getItem(position)
        view.findViewById<TextView>(android.R.id.text1).apply {
            text = "${ticket?.from} → ${ticket?.to} - ${ticket?.price} руб"
            setLines(1)
        }

        return view
    }

    override fun getCount(): Int = tickets.size
}
