package com.parassidhu.coronavirusapp.ui.india

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.base.BaseFragment
import com.parassidhu.coronavirusapp.network.response.BaseCountryResponse
import com.parassidhu.coronavirusapp.network.response.StatewiseResult
import com.parassidhu.coronavirusapp.ui.main.MainViewModel
import com.parassidhu.coronavirusapp.ui.main.adapter.StandardListAdapter
import com.parassidhu.coronavirusapp.util.*
import kotlinx.android.synthetic.main.fragment_india.*

class IndiaFragment : BaseFragment(), StandardListAdapter.OnEvent {

    private val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    private val listAdapter  by lazy { StandardListAdapter(mutableListOf(), this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_india, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        showLoading(true)
        setupRecyclerView()
        setupObservers()
        setClickListeners()
    }

    private fun setClickListeners() {
        helpImageView.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.thanks_title))
                .setMessage(getString(R.string.thanks_message))
                .create()
                .show()
        }

        searchImageView.setOnClickListener {
            showSearch(true)
            runInHandler(200) { showKeyboard(searchEditText, true) }
            searchEditText.requestFocus()
        }

        searchEditText.doAfterTextChanged { text: Editable? ->
            listAdapter.search(text.toString(), false)
        }

        backButton.setOnClickListener {
            showSearch(false)
            showKeyboard(searchEditText, false)
            searchEditText.setText("")
        }
    }

    private fun showSearch(flag: Boolean) {
        searchBar.isInvisible = !flag
        backButton.isInvisible = !flag
        searchImageView.isInvisible = flag
        tvIndiaStats.isInvisible = flag
        helpImageView.isInvisible = flag
        groupViews.isGone = flag

        if (!flag) {
            listAdapter.search("", false)
        }
    }

    private fun setupObservers() {
        viewModel.stateResponse.observe(viewLifecycleOwner) { list ->
            listAdapter.clear()
            val newList = filterTotalAndReturn(list.toMutableList())
            listAdapter.addData(newList)
            showLoading(false)
        }
    }

    private fun filterTotalAndReturn(list: MutableList<StatewiseResult>): List<StatewiseResult> {
        for (index in list.indices) {
            val item = list[index]
            if (item.stateName == "Total") {
                setupCounts(item)
                list.removeAt(index)
                break
            }
        }

        return list
    }

    private fun setupCounts(item: StatewiseResult) {
        confirmedCount.text = item.totalConfirmed
        recoveredCount.text = item.totalRecovered
        deathCount.text = item.totalDeaths
    }

    private fun setupRecyclerView() {
        stateRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }

    private fun showLoading(flag: Boolean) {
        appBarLayout.isVisible = !flag

        if (flag)
            shimmerLoading.start()
        else
            shimmerLoading.stop()
    }

    fun onSort(sortOrder: SortEnum) {
        listAdapter.sort(sortOrder)
    }

    override fun logEvent(query: String) {
    }

    override fun onPinClick(response: BaseCountryResponse, isPinned: Boolean) {
    }
}
