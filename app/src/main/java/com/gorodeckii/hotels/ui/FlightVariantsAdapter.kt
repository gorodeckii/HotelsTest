package com.gorodeckii.hotels.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorodeckii.hotels.R
import com.gorodeckii.hotels.api.models.inner.FlightVariant
import io.reactivex.functions.Consumer

class FlightVariantsAdapter(private val items: List<FlightVariant>, private val clickListener: Consumer<FlightVariant>) : RecyclerView.Adapter<FlightVariantsAdapter.FlightVariantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightVariantViewHolder {
        return FlightVariantViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_flight_variant, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FlightVariantViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class FlightVariantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val companyTextView = itemView.findViewById<TextView>(R.id.item_flight_variant_company)
        private val priceTextView = itemView.findViewById<TextView>(R.id.item_flight_variant_price)

        fun bind(item: FlightVariant) {
            itemView.setOnClickListener { clickListener.accept(item) }
            companyTextView.text = item.company.name
            priceTextView.text = String.format(itemView.context.getString(R.string.price_placeholder), item.price)
        }

    }

}