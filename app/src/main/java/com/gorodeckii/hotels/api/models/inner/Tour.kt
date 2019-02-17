package com.gorodeckii.hotels.api.models.inner

import com.gorodeckii.hotels.api.models.Flight
import com.gorodeckii.hotels.api.models.Hotel

data class Tour(val hotel: Hotel, val flights: Array<Flight>, val minTotalPrice: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tour

        if (hotel != other.hotel) return false
        if (!flights.contentEquals(other.flights)) return false
        if (minTotalPrice != other.minTotalPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hotel.hashCode()
        result = 31 * result + flights.contentHashCode()
        result = 31 * result + minTotalPrice
        return result
    }

}