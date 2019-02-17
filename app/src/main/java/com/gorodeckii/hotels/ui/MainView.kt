package com.gorodeckii.hotels.ui

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gorodeckii.hotels.api.models.Company
import com.gorodeckii.hotels.api.models.inner.FlightVariant
import com.gorodeckii.hotels.api.models.inner.Tour

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun showTours(tours: List<Tour>, isFiltered: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFlightVariants(flightVariants: List<FlightVariant>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showFilter(companies: Array<Company>)

    @StateStrategyType(SkipStrategy::class)
    fun showMessage(@StringRes messageId: Int)

}