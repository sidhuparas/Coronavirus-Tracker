package com.parassidhu.coronavirusapp.ui.overview

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.base.BaseFragment
import com.parassidhu.coronavirusapp.network.response.*
import com.parassidhu.coronavirusapp.ui.about.AboutPopup
import com.parassidhu.coronavirusapp.ui.main.MainViewModel
import com.parassidhu.coronavirusapp.ui.main.adapter.StandardListAdapter
import com.parassidhu.coronavirusapp.util.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlin.random.Random

class OverviewFragment : BaseFragment(), AppBarLayout.OnOffsetChangedListener,
    StandardListAdapter.OnEvent {

    private val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    private val listAdapter by lazy { StandardListAdapter(mutableListOf(), this) }
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun setupObservers() {
        viewModel.combinedLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                listAdapter.clear()
                listAdapter.addData(list)
                showLoading(false)
            }
        }

        viewModel.worldStats.observe(viewLifecycleOwner) { response ->
            setupWorldStats(response)
        }

        viewModel.bannerResponse.observe(viewLifecycleOwner) { list ->
            setupBanner(list)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            if (error != null)
                requireContext().toast(error)
        }
    }

    private fun setupBanner(list: List<BannerResult>) {
        handler.post(object : Runnable {
            override fun run() {
                val randomNumber = Random.nextInt(0, list.size)
                val randomItem = list[randomNumber]
                loadBanner(randomItem)
                handler.postDelayed(this, 10000)
            }
        })
    }

    private fun loadBanner(randomItem: BannerResult) {
        if (activity == null || activity?.isDestroyed == true || this.isDetached || this.isRemoving)
            return

        Glide.with(this)
            .asBitmap()
            .load(randomItem.image)
            .placeholder(R.drawable.placeholder)
            .apply(cornerRadius(10))
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bannerImage.setImageBitmap(resource)
                    val set = ConstraintSet()
                    set.clone(rootLayout)
                    set.setDimensionRatio(bannerImage.id, randomItem.ratio)
                    set.applyTo(rootLayout)
                }
            })
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
            listAdapter.search(text.toString(), true)
        }

        swipeToRefresh.setOnRefreshListener {
            listAdapter.clear()
            makeApiCalls()
            swipeToRefresh.isRefreshing = false
        }

        hamburgerImageView.setOnClickListener {
            AboutPopup.Builder(requireContext())
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
            layoutManager = LinearLayoutManager(requireContext())
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
            listAdapter.search("", true)
        }
    }

    fun scrollToTop() {
        countryWiseRecyclerView?.smoothScrollToPosition(0)
        appBarLayout?.setExpanded(true, true)
    }

    fun handleBackPress(): Boolean {
        if (searchBar.isVisible) {
            showSearch(false)
            return true
        }

        return false
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

    override fun onPinClick(response: BaseCountryResponse, isPinned: Boolean) {
        if (isPinned) {
            if (response is CountryStat)
                viewModel.addToFavorite(Utils.toFavorite(response))
            else if (response is FavoriteCountry)
                viewModel.addToFavorite(response)
        } else {
            if (response is CountryStat)
                viewModel.removeFromFavorite(Utils.toFavorite(response))
            else if (response is FavoriteCountry)
                viewModel.removeFromFavorite(response)
        }
    }
}
