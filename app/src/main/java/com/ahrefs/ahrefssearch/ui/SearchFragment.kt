package com.ahrefs.ahrefssearch.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
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
    val SEARCH_TERM: String = "SEARCH_TERM"
    val INTENT_EXTRA = "Search_Extra"
    private var errorMessage: String = "Error in retrieving data"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[SearchSuggestionViewModel::class.java]

        edtSearch = binding.appBarLayout.edtSearch

        binding.recyclerViewLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchSuggestionAdapter(this)

        //get data from activity via bundle
        val bundle = arguments
        val searchTerm = bundle!!.getString(SEARCH_TERM)

        //set search term to search box
        edtSearch.setText(searchTerm)

        //search according to search term
        search(viewModel, searchTerm.toString())

        //Search when text changes
        edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if(editable?.length!! > 0){
                    search(viewModel, editable.toString())
                    binding.appBarLayout.imgBack.visibility = View.VISIBLE
                    binding.appBarLayout.imgSearch.visibility = View.GONE
                    binding.appBarLayout.imgClose.visibility = View.VISIBLE
                }
            }

        })

        //Hide soft keyboard when search icon from soft keyboard is clicked
        edtSearch.setOnEditorActionListener { _, _, _ ->
            edtSearch.clearFocus()
            context?.hideKeyboard(binding.root)
            true
        }

        //Close activity when back icon is clicked
        binding.appBarLayout.imgBack.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(INTENT_EXTRA, edtSearch.text.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        //Clear text when cross icon is clicked
        binding.appBarLayout.imgClose.setOnClickListener{
            edtSearch.text.clear()
        }

        //add divider for recycler view items
        binding.recyclerViewLayout.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        return binding.root
    }

    private fun search(viewModel: SearchSuggestionViewModel, searchKeyword: String) {

        viewModel.searchKeyword = searchKeyword
        viewModel.getLiveDataObserver().observe(viewLifecycleOwner, Observer { result->
            if(result != null){
                binding.recyclerViewLayout.recyclerView.adapter = adapter
                adapter.setSearchSuggestion(result)
                if(result.isNotEmpty()) {
                    binding.emptyLayout.txtEmpty.visibility = View.GONE
                }else{
                    binding.emptyLayout.txtEmpty.visibility = View.VISIBLE
                }
            }else{
                binding.emptyLayout.txtEmpty.visibility = View.VISIBLE
                binding.emptyLayout.txtEmpty.text = errorMessage
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

                //place the cursor at the end of text
                edtSearch.setSelection(searchKeyword.length)
            }
        }
    }
}