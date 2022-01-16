package com.ahrefs.ahrefssearch.ui

import android.view.View

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, searchKeyword: String)
}