package com.ahrefs.ahrefssearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahrefs.ahrefssearch.R
import com.google.android.material.textview.MaterialTextView

class SearchSuggestionAdapter: RecyclerView.Adapter<SearchSuggestionAdapter.SearchSuggestionViewHolder>() {

    private var searchSuggestion: List<String>? = listOf("Hello", "Hi")

    fun setSearchSuggestion(searchSuggestion: List<String>){
        this.searchSuggestion = searchSuggestion
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_search_item, parent, false)
        return SearchSuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        holder.bind(searchSuggestion?.get(position)!!)
    }

    override fun getItemCount(): Int = searchSuggestion?.size!!

    class SearchSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(suggestion: String){
            itemView.findViewById<MaterialTextView>(R.id.txtSuggestion).text = suggestion
        }
    }
}