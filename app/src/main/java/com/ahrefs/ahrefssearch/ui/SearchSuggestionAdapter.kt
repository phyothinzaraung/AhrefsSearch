package com.ahrefs.ahrefssearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahrefs.ahrefssearch.databinding.LayoutSearchItemBinding

class SearchSuggestionAdapter(
    private val listener: RecyclerViewClickListener
): RecyclerView.Adapter<SearchSuggestionAdapter.SearchSuggestionViewHolder>() {

    private lateinit var searchSuggestion: List<String>

    fun setSearchSuggestion(searchSuggestion: List<String>){
        this.searchSuggestion = searchSuggestion
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionViewHolder {
        val binding = LayoutSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        holder.bind(searchSuggestion?.get(position)!!, listener)
    }

    override fun getItemCount(): Int = searchSuggestion?.size!!

    class SearchSuggestionViewHolder(private val binding: LayoutSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: String, listener: RecyclerViewClickListener){

            binding.apply {
                txtSuggestion.text = suggestion
                imgDiagonal.setOnClickListener{listener.onRecyclerViewItemClick(imgDiagonal, suggestion)}
            }
        }
    }
}
