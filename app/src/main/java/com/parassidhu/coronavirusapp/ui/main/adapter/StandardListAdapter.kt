package com.parassidhu.coronavirusapp.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.base.BaseRecyclerViewAdapter
import com.parassidhu.coronavirusapp.network.response.BaseCountryResponse
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.FavoriteCountry
import com.parassidhu.coronavirusapp.network.response.StatewiseResult
import com.parassidhu.coronavirusapp.util.SortEnum
import com.parassidhu.coronavirusapp.util.SortEnum.*
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_list.view.cardRootView
import kotlinx.android.synthetic.main.item_list.view.confirmedCount
import kotlinx.android.synthetic.main.item_list.view.deathCount
import kotlinx.android.synthetic.main.item_list.view.newCasesCount
import kotlinx.android.synthetic.main.item_list.view.recoveredCount
import kotlinx.android.synthetic.main.item_state.view.*

class StandardListAdapter(
    private val list: MutableList<BaseCountryResponse>,
    private val listener: OnEvent
): BaseRecyclerViewAdapter() {

    private var originalList = mutableListOf<BaseCountryResponse>()

    private val TYPE_FAVORITE = 1
    private val TYPE_NORMAL = 2
    private val TYPE_STATE = 3

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is FavoriteCountry)
            TYPE_FAVORITE
        else if (list[position] is StatewiseResult)
            TYPE_STATE
        else
            TYPE_NORMAL
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = getView(R.layout.item_list, parent)
        val stateView = getView(R.layout.item_state, parent)

        return if (viewType == TYPE_NORMAL)
            CountryWiseViewHolder(view)
        else if (viewType == TYPE_FAVORITE)
            FavoriteViewHolder(view)
        else
            StateViewHolder(stateView)
    }

    fun addData(list: List<BaseCountryResponse>) {
        this.list.addAll(list)
        originalList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        originalList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryWiseViewHolder)
            holder.bind(list[position] as CountryStat)
        else if (holder is FavoriteViewHolder)
            holder.bind(list[position] as FavoriteCountry)
        else if (holder is StateViewHolder)
            holder.bind(list[position] as StatewiseResult)
    }

    fun search(query: String, isCountry: Boolean) {
        list.clear()
        this.list.addAll(originalList)

        if (query.isNotEmpty()) {
            var index = 0

            if (isCountry) {
                while (index < list.size) {
                    val item = list[index]
                    val text = if (item is FavoriteCountry)
                        item.countryName
                    else
                        (item as CountryStat).countryName

                    if (text.toLowerCase().contains(query.toLowerCase()).not()) {
                        list.removeAt(index)
                        index--
                    }
                    index++
                }
                listener.logEvent(query)
            } else {
                while (index < list.size) {
                    val item = list[index] as StatewiseResult
                    val text = item.stateName

                    if (text.toLowerCase().contains(query.toLowerCase()).not()) {
                        list.removeAt(index)
                        index--
                    }
                    index++
                }
            }
        }

        notifyDataSetChanged()
    }

    fun sort(sortOrder: SortEnum) {
         when(sortOrder) {
            ALPHABETICAL -> {
                list.sortBy {
                    if (it is StatewiseResult)
                        it.stateName
                    else
                        ""
                }
            }
            ASCENDING -> {
                list.sortBy {
                    if (it is StatewiseResult)
                        it.totalConfirmed.toInt()
                    else
                        1
                }
            }
            DESCENDING -> {
                list.sortBy {
                    if (it is StatewiseResult)
                        -it.totalConfirmed.toInt()
                    else
                        1
                }
            }
        }

        notifyDataSetChanged()
    }

    inner class StateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(result: StatewiseResult) {
            itemView.apply {
                stateName.text = result.stateName.toUpperCase()
                newCasesCount.text = result.deltaConfirmed.toString()
                confirmedCount.text = result.totalConfirmed
                recoveredCount.text = result.totalRecovered
                deathCount.text = result.totalDeaths

                trendIcon.isVisible = true

                if (result.deltaConfirmed > 0)
                    Glide.with(itemView).load(R.drawable.ic_up_red).into(trendIcon)
                else if (result.deltaConfirmed < 0)
                    Glide.with(itemView).load(R.drawable.ic_down_green).into(trendIcon)
                else
                    trendIcon.isVisible = false
            }
        }
    }

    inner class CountryWiseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(countryStat: CountryStat) {
            itemView.apply {
                countryName.text = countryStat.countryName.toUpperCase()
                newCasesCount.text = countryStat.newCases
                confirmedCount.text = countryStat.totalCases
                recoveredCount.text = countryStat.totalRecovered
                deathCount.text = countryStat.totalDeaths

                pinImageView.setOnClickListener {
                    listener.onPinClick(countryStat, true)
                    pinImageView.isVisible = false
                    pinnedImageView.isVisible = true
                }

                pinnedImageView.setOnClickListener {
                    listener.onPinClick(countryStat, false)
                    pinImageView.isVisible = true
                    pinnedImageView.isVisible = false
                }
            }
        }
    }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(favoriteCountry: FavoriteCountry) {
            itemView.apply {
                countryName.text = favoriteCountry.countryName.toUpperCase()
                newCasesCount.text = favoriteCountry.newCases
                confirmedCount.text = favoriteCountry.totalCases
                recoveredCount.text = favoriteCountry.totalRecovered
                deathCount.text = favoriteCountry.totalDeaths

                pinImageView.setOnClickListener {
                    listener.onPinClick(favoriteCountry, true)
                    pinImageView.isVisible = false
                    pinnedImageView.isVisible = true
                }

                pinnedImageView.setOnClickListener {
                    listener.onPinClick(favoriteCountry, false)
                    pinImageView.isVisible = true
                    pinnedImageView.isVisible = false
                }

                pinnedImageView.isVisible = true
                pinImageView.isVisible = false

                cardRootView.setBackgroundResource(R.drawable.bg_outline)
            }
        }
    }

    interface OnEvent {
        fun logEvent(query: String)
        fun onPinClick(response: BaseCountryResponse, isPinned: Boolean)
    }
}