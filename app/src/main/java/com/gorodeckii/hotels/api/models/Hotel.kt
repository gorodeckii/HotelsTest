package com.gorodeckii.hotels.api.models

data class Hotel(val id: Int, val flights: Array<Int>, val name: String, val price: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hotel

        if (id != other.id) return false
        if (!flights.contentEquals(other.flights)) return false
        if (name != other.name) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + flights.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + price
        return result
    }

}