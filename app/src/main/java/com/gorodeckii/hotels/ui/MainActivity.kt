package com.gorodeckii.hotels.ui

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gorodeckii.hotels.R
import com.gorodeckii.hotels.api.models.Company
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

    private lateinit var filterSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        filterSnackbar = Snackbar.make(root_view, R.string.list_filtered, Snackbar.LENGTH_INDEFINITE)

        fab.setOnClickListener {
            presenter.filterClicked()
        }

        main_recycler.layoutManager = LinearLayoutManager(this)
        main_recycler.adapter = adapter

        main_refresh_layout.setOnRefreshListener { presenter.getInfo() }

        presenter.getInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        if (item?.itemId == R.id.menu_clear_filter) {
            presenter.clearFilterClicked()
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
        filterSnackbar.dismiss()
    }

    override fun showTours(tours: List<Tour>, isFiltered: Boolean) {
        adapter.setBottomPaddingVisible(isFiltered)
        if (isFiltered) {
            filterSnackbar.show()
        } else {
            filterSnackbar.dismiss()
        }
        adapter.items = tours
    }

    override fun hideProgress() {
        main_progress_bar.visibility = View.GONE
        main_recycler.visibility = View.VISIBLE
        main_refresh_layout.isRefreshing = false
    }

    override fun showFlightVariants(flightVariants: List<FlightVariant>) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_flight_variants, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.flight_variants_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FlightVariantsAdapter(flightVariants, Consumer {
            dialog?.dismiss()
            Toast.makeText(this, String.format(getString(R.string.flight_chosen_placeholder), it.company.name), Toast.LENGTH_LONG).show()
        })
        dialog = AlertDialog.Builder(this).setTitle(R.string.choose_flight)
            .setView(view).create()
        dialog?.show()
    }

    override fun showFilter(companies: Array<Company>) {
        val companyNames = Array(companies.size) { companies[it].name }
        dialog = AlertDialog.Builder(this).setTitle(R.string.filter).setItems(companyNames) { dialog, which ->
            dialog.dismiss()
            presenter.filterCompanySelected(companies[which])
        }.create()
        dialog?.show()
    }

    override fun showMessage(@StringRes messageId: Int) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
    }

}
