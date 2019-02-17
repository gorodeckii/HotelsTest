package com.gorodeckii.hotels.ui

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gorodeckii.hotels.R
import com.gorodeckii.hotels.api.models.inner.Tour
import com.gorodeckii.hotels.utils.getCorrespondingFlightVariantsString
import io.reactivex.functions.Consumer

class ToursAdapter(val clickListener: Consumer<Tour>) : RecyclerView.Adapter<ToursAdapter.TourViewHolder>() {

    private var bottomPaddingVisible: Boolean = false

    var items: List<Tour> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        return TourViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tour, parent, false))
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun setBottomPaddingVisible(isVisible: Boolean) {
        bottomPaddingVisible = isVisible
    }

    inner class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cardView = itemView.findViewById<CardView>(R.id.item_tour_card)
        private val hotelNameView = itemView.findViewById<TextView>(R.id.item_tour_hotel_name)
        private val flightNumberView = itemView.findViewById<TextView>(R.id.item_tour_flight_number)
        private val priceView = itemView.findViewById<TextView>(R.id.item_tour_price)
        private val paddingView = itemView.findViewById<View>(R.id.item_tour_padding)

        fun bind(tour: Tour, position: Int) {
            cardView.setOnClickListener { clickListener.accept(tour) }
            hotelNameView.text = String.format(itemView.context.getString(R.string.hotel_placeholder), tour.hotel.name)
            flightNumberView.text = String.format(
                itemView.context.getString(getCorrespondingFlightVariantsString(tour.flights.size)),
                tour.flights.size
            )
            priceView.text = if (tour.flights.size > 1) String.format(
                itemView.context.getString(R.string.min_price_placeholder),
                tour.minTotalPrice
            ) else String.format(
                itemView.context.getString(R.string.price_placeholder),
                tour.minTotalPrice
            )
            paddingView.visibility = if (position == items.size - 1 && bottomPaddingVisible) View.VISIBLE else View.GONE
        }

    }

}