package com.parassidhu.coronavirusapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.network.response.BaseCountryResponse
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.FavoriteCountry
import kotlinx.android.synthetic.main.item_list.view.*

class CountryWiseAdapter(
    private val list: MutableList<BaseCountryResponse>,
    private val listener: OnEvent
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var originalList = mutableListOf<BaseCountryResponse>()

    private val TYPE_FAVORITE = 1
    private val TYPE_NORMAL = 2

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is FavoriteCountry)
            TYPE_FAVORITE
        else
            TYPE_NORMAL
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return if (viewType == TYPE_NORMAL)
            CountryWiseViewHolder(view)
        else
            FavoriteViewHolder(view)
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
    }

    fun search(query: String) {
        list.clear()
        this.list.addAll(originalList)

        if (query.isNotEmpty()) {
            var index = 0

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
        }

        notifyDataSetChanged()
    }

    inner class CountryWiseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(countryStat: CountryStat) {
            itemView.apply {
                countryName.text = countryStat.countryName
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
                countryName.text = favoriteCountry.countryName
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
            }
        }
    }

    interface OnEvent {
        fun logEvent(query: String)
        fun onPinClick(response: BaseCountryResponse, isPinned: Boolean)
    }
}