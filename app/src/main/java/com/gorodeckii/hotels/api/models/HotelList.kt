package com.gorodeckii.hotels.api.models

data class HotelList(val hotels: Array<Hotel>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HotelList

        if (!hotels.contentEquals(other.hotels)) return false

        return true
    }

    override fun hashCode(): Int {
        return hotels.contentHashCode()
    }

}