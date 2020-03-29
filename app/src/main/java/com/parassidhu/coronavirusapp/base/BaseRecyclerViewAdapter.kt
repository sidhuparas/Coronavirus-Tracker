package com.parassidhu.coronavirusapp.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected fun getView(@LayoutRes layoutId: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }
}