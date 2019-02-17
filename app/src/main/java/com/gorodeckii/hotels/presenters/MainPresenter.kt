package com.gorodeckii.hotels.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gorodeckii.hotels.R
import com.gorodeckii.hotels.api.RetrofitProvider
import com.gorodeckii.hotels.api.models.Company
import com.gorodeckii.hotels.api.models.CompanyList
import com.gorodeckii.hotels.api.models.FlightList
import com.gorodeckii.hotels.api.models.HotelList
import com.gorodeckii.hotels.api.models.inner.FlightVariant
import com.gorodeckii.hotels.api.models.inner.Tour
import com.gorodeckii.hotels.ui.MainView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    private var companyList: CompanyList? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var currentUnfilteredTours: List<Tour>? = null

    fun getInfo() {

        val api = RetrofitProvider.api

        val disposable = Single.zip(api.getFlights(), api.getHotels(), api.getCompanies(), Function3<FlightList, HotelList, CompanyList, List<Tour>> { flightList, hotelList, companyList ->
            this.companyList = companyList
            val tours = ArrayList<Tour>()
            for (hotel in hotelList.hotels) {
                val flights = flightList.flights.filter { flight -> hotel.flights.contains(flight.id)}
                var minTotalPrice = 0
                flights.forEach {
                    if (minTotalPrice == 0) {
                        minTotalPrice = hotel.price + it.price
                    }
                    if ((hotel.price + it.price) < minTotalPrice ) {
                        minTotalPrice = hotel.price + it.price
                    }
                }
                tours.add(Tour(hotel, flights.toTypedArray(), minTotalPrice))
            }
            tours
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ result: List<Tour> ->
            currentUnfilteredTours = result
            viewState.showTours(result, false)
            viewState.hideProgress()
        }, {
            viewState.showMessage(R.string.data_load_error)
            viewState.hideProgress()
        })

        compositeDisposable.add(disposable)

    }

    fun tourClicked(tour: Tour) {
        companyList?.let {
            val flightVariants = ArrayList<FlightVariant>()
            for (flight in tour.flights) {
                val company = it.companies.firstOrNull { company -> company.id == flight.companyId }
                if (company != null) {
                    flightVariants.add(FlightVariant(company, tour.hotel.price + flight.price))
                }
            }
            viewState.showFlightVariants(flightVariants)
        } ?: kotlin.run {
            viewState.showMessage(R.string.companies_error)
        }
    }

    fun filterClicked() {
        companyList?.let {
            viewState.showFilter(it.companies)
        } ?: kotlin.run {
            viewState.showMessage(R.string.companies_error)
        }
    }

    fun filterCompanySelected(company: Company) {
        val filteredTours = currentUnfilteredTours?.filter { it.flights.any { flight -> flight.companyId == company.id } }
        if (filteredTours != null) {
            viewState.showTours(filteredTours, true)
        }
    }

    fun clearFilterClicked() {
        currentUnfilteredTours?.let { viewState.showTours(it, false) }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}