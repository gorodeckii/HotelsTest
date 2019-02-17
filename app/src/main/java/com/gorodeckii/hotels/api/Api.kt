package com.gorodeckii.hotels.api

import com.gorodeckii.hotels.api.models.CompanyList
import com.gorodeckii.hotels.api.models.FlightList
import com.gorodeckii.hotels.api.models.HotelList
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET("zqxvw")
    fun getFlights(): Single<FlightList>

    @GET("12q3ws")
    fun getHotels(): Single<HotelList>

    @GET("8d024")
    fun getCompanies(): Single<CompanyList>

}