package com.parassidhu.coronavirusapp.ui.india

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.base.BaseFragment
import com.parassidhu.coronavirusapp.network.response.BaseCountryResponse
import com.parassidhu.coronavirusapp.ui.main.MainViewModel
import com.parassidhu.coronavirusapp.ui.main.adapter.StandardListAdapter
import kotlinx.android.synthetic.main.fragment_india.*
import androidx.lifecycle.observe
import com.parassidhu.coronavirusapp.ui.main.MainActivity
import com.parassidhu.coronavirusapp.util.SortEnum

class IndiaFragment : BaseFragment(), StandardListAdapter.OnEvent {

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

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
        setupRecyclerView()
        setupObservers()
        viewModel.getStatewiseStats()
    }

    private fun setupObservers() {
        viewModel.stateResponse.observe(viewLifecycleOwner) { list ->
            listAdapter.clear()
            listAdapter.addData(list)
        }
    }

    private fun setupRecyclerView() {
        stateRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }

    fun onSort(sortOrder: SortEnum) {
        when(sortOrder) {
            SortEnum.ALPHABETICAL -> {
                listAdapter.sort()
            }

            SortEnum.ASCENDING -> {

            }

            SortEnum.DESCENDING -> {

            }
        }
    }

    override fun logEvent(query: String) {
    }

    override fun onPinClick(response: BaseCountryResponse, isPinned: Boolean) {
    }
}
