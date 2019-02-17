package com.gorodeckii.hotels.utils

import android.support.annotation.StringRes
import com.gorodeckii.hotels.R

@StringRes
fun getCorrespondingFlightVariantsString(flightsCount: Int): Int {
    var localCount = flightsCount
    while (localCount > 10) {
        localCount %= 10
    }
    return when (localCount) {
        1 -> R.string.flight_variants_placeholder_one
        2,3,4 -> R.string.flight_variants_placeholder_two
        else -> R.string.flight_variants_placeholder_many
    }
}