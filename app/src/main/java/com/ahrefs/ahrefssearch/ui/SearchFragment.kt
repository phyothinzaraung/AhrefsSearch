package com.ahrefs.ahrefssearch.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahrefs.ahrefssearch.R
import com.ahrefs.ahrefssearch.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), RecyclerViewClickListener{
    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchSuggestionAdapter
    private lateinit var viewModel: SearchSuggestionViewModel
    private lateinit var edtSearch: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[SearchSuggestionViewModel::class.java]

        edtSearch = binding.edtSearch

        edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if(editable?.length!! > 0){
                    search(viewModel, editable.toString())
                    binding.imgBack.visibility = View.VISIBLE
                    binding.imgSearch.visibility = View.GONE
                    binding.imgClose.visibility = View.VISIBLE
                }
            }

        })

        edtSearch.setOnEditorActionListener { _, _, _ ->
            edtSearch.clearFocus()
            context?.hideKeyboard(binding.root)
            true
        }

        binding.imgBack.setOnClickListener {
            activity?.finish()
        }

        binding.imgClose.setOnClickListener{
            edtSearch.text.clear()
        }

        return binding.root
    }

    private fun search(viewModel: SearchSuggestionViewModel, searchKeyword: String) {

        viewModel.searchKeyword = searchKeyword
        viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                adapter = SearchSuggestionAdapter(this)
                binding.recyclerView.adapter = adapter
                adapter.setSearchSuggestion(it)
            }else{
                Toast.makeText(context, "error in retrieving data...", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.loadSearchSuggestionData()


    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onRecyclerViewItemClick(view: View, searchKeyword: String) {
        when(view.id){
            R.id.imgDiagonal -> {
                edtSearch.setText(searchKeyword)
            }
        }
    }
}