package com.ahrefs.ahrefssearch.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahrefs.ahrefssearch.AhrefsSearchApplication
import com.ahrefs.ahrefssearch.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: SearchSuggestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        initRecyclerView()
        initViewModel()
        return view
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchSuggestionAdapter()
        recyclerView.adapter = adapter
    }

//    private fun initViewModel(){
//        viewModel.suggestions.observe(viewLifecycleOwner){ result ->
//            Log.v("result", result.toString())
//            adapter.setSearchSuggestion(result)
//        }
//    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(SearchSuggestionViewModel::class.java)
        viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if(it != null){
                adapter.setSearchSuggestion(it)
            }else{
                Toast.makeText(context, "error in retrieving data...", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.loadSearchSuggestionData()

    }
}