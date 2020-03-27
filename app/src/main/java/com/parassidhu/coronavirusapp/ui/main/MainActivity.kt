package com.parassidhu.coronavirusapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.parassidhu.coronavirusapp.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.parassidhu.coronavirusapp.base.BaseActivity
import com.parassidhu.coronavirusapp.network.response.WorldStats
import com.parassidhu.coronavirusapp.ui.about.AboutPopup
import com.parassidhu.coronavirusapp.ui.main.adapter.CountryWiseAdapter
import com.parassidhu.coronavirusapp.util.*

class MainActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener, CountryWiseAdapter.OnEvent {

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private val listAdapter by lazy { CountryWiseAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        loadBanner()
    }

    private fun setupObservers() {
        viewModel.countryWiseCasesResponse.observe(this) { list ->
            listAdapter.addData(list)
            showLoading(false)
        }

        viewModel.worldStats.observe(this) { response ->
            setupWorldStats(response)
        }

        viewModel.errorLiveData.observe(this) { error ->
            if (error != null)
                toast(error)
        }
    }

    private fun loadBanner() {
        Glide.with(this)
            .load(glideUrl)
            .placeholder(R.drawable.placeholder)
            .apply(cornerRadius(10))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(bannerImage)
    }

    private fun init() {
        showLoading(true)
        setupObservers()
        setupRecyclerView()
        setListeners()
    }

    private fun setListeners() {
        searchImageView.setOnClickListener {
            showSearch(true)
            runInHandler(200) { showKeyboard(searchEditText, true) }
            searchEditText.requestFocus()
        }

        backButton.setOnClickListener {
            showSearch(false)
            showKeyboard(searchEditText, false)
            searchEditText.setText("")
        }

        searchEditText.doAfterTextChanged { text: Editable? ->
            listAdapter.search(text.toString())
        }

        swipeToRefresh.setOnRefreshListener {
            listAdapter.clear()
            makeApiCalls()
            loadBanner()
            swipeToRefresh.isRefreshing = false
        }

        hamburgerImageView.setOnClickListener {
            AboutPopup.Builder(this)
                .setGravity(Gravity.CENTER)
                .setTintColor((Color.parseColor("#80000000")))
                .build()
                .show()
        }
    }

    private fun makeApiCalls() {
        viewModel.getCountryWiseCases()
        viewModel.getWorldStats()
    }

    private fun setupRecyclerView() {
        countryWiseRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter
        }
    }

    private fun setupWorldStats(response: WorldStats) {
        val weight = Utils.provideBarWeights(response)

        Glide.with(this).load(R.drawable.yellow_bar).apply(cornerRadius(2)).into(yellowBar)
        Glide.with(this).load(R.drawable.green_bar).apply(cornerRadius(2)).into(greenBar)
        Glide.with(this).load(R.drawable.red_bar).apply(cornerRadius(2)).into(redBar)

        yellowBar.setWeight(weight.first)
        greenBar.setWeight(weight.second)
        redBar.setWeight(weight.third)

        barContainer.weightSum = weight.first + weight.second + weight.third

        response.apply {
            confirmedCount.text = totalCases
            recoveredCount.text = totalRecovered
            deathCount.text = totalDeath
        }
    }

    private fun showLoading(flag: Boolean) {
        countryWiseRecyclerView.isVisible = !flag
        toolbarViews.isVisible = !flag

        if (flag)
            shimmerLoading.start()
        else
            shimmerLoading.stop()
    }

    private fun showSearch(flag: Boolean) {
        searchBar.isInvisible = !flag
        backButton.isInvisible = !flag
        hamburgerImageView.isInvisible = flag
        searchImageView.isInvisible = flag
        toolbarViews.isVisible = !flag
        bannerImage.isVisible = !flag
        titleLogo.isVisible = !flag
        swipeToRefresh.isEnabled = !flag

        if (!flag) {
            listAdapter.search("")
        }
    }

    override fun onBackPressed() {
        if (searchBar.isVisible)
            backButton.callOnClick()
        else
            super.onBackPressed()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeToRefresh.isEnabled = verticalOffset == 0 && !searchBar.isVisible
    }

    override fun onResume() {
        super.onResume()
        appBarLayout?.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appBarLayout?.removeOnOffsetChangedListener(this)
    }

    override fun logEvent(query: String) {
        val params = Bundle()
        params.putString("query", query)
        firebaseAnalytics.logEvent("search_query", params)
    }
}