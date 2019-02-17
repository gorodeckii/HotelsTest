package com.gorodeckii.hotels.ui

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gorodeckii.hotels.R
import com.gorodeckii.hotels.api.models.inner.FlightVariant
import com.gorodeckii.hotels.api.models.inner.Tour
import com.gorodeckii.hotels.presenters.MainPresenter
import io.reactivex.functions.Consumer

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val adapter: ToursAdapter = ToursAdapter(Consumer { presenter.tourClicked(it) } )

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            //TODO фильтр
        }

        main_recycler.layoutManager = LinearLayoutManager(this)
        main_recycler.adapter = adapter

        presenter.getInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }

    override fun showTours(tours: List<Tour>) {
        adapter.items = tours
    }

    override fun showFlightVariants(flightVariants: List<FlightVariant>) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_flight_variants, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.flight_variants_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FlightVariantsAdapter(flightVariants, Consumer {
            dialog?.dismiss()
            Snackbar.make(root_view, String.format(getString(R.string.flight_chosen_placeholder), it.company.name), Snackbar.LENGTH_LONG).show()
        })
        dialog = AlertDialog.Builder(this).setTitle(R.string.choose_flight)
            .setView(view).create()
        dialog?.show()
    }

    override fun showMessage(@StringRes messageId: Int) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
    }

}
