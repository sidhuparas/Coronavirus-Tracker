package com.parassidhu.coronavirusapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parassidhu.coronavirusapp.R
import com.parassidhu.coronavirusapp.network.response.CountryStat
import kotlinx.android.synthetic.main.item_list.view.*

class CountryWiseAdapter(
    private val list: MutableList<CountryStat>,
    private val listener: OnEvent
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var originalList = mutableListOf<CountryStat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return CountryWiseViewHolder(view)
    }

    fun addData(list: List<CountryStat>) {
        this.list.addAll(list)
        originalList.addAll(list)
        filterIndia(list)
        notifyDataSetChanged()
    }

    private fun filterIndia(list: List<CountryStat>) {
        var indiaIndex = -1
        var indiaItem: CountryStat? = null

        for (index in list.indices) {
            if (list[index].countryName == "India") {
                indiaIndex = index
                indiaItem = list[index]
                break
            }
        }

        if (indiaIndex != -1 && indiaItem != null) {
            this.list.add(0, indiaItem)
            this.list.removeAt(indiaIndex + 1)

            originalList.add(0, indiaItem)
            originalList.removeAt(indiaIndex + 1)
        }
    }

    fun clear() {
        list.clear()
        originalList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryWiseViewHolder)
            holder.bind(list[position])
    }

    fun search(query: String) {
        list.clear()
        this.list.addAll(originalList)

        if (query.isNotEmpty()) {
            var index = 0

            while (index < list.size) {
                if (list[index].countryName.toLowerCase().contains(query.toLowerCase()).not()) {
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
            }
        }
    }

    interface OnEvent {
        fun logEvent(query: String)
    }
}