package com.ahrefs.ahrefssearch.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahrefs.ahrefssearch.R
import com.ahrefs.ahrefssearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RecyclerViewClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchSuggestionViewModel
    private lateinit var adapter: SearchSuggestionAdapter
    private lateinit var edtSearch: EditText
    private lateinit var searchedString: String
    private var errorMessage: String = "Error in retrieving data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SearchSuggestionViewModel::class.java]
        edtSearch = binding.appBarLayout.edtSearch

        binding.recyclerViewLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchSuggestionAdapter(this)

        //getIntent
        searchedString = intent?.getStringExtra(SearchFragment().INTENT_EXTRA).toString()
        if(searchedString == "null" || searchedString.isEmpty()){
            edtSearch.setText("")
        }else{
            binding.mainLayout.visibility = View.VISIBLE
            edtSearch.setText(searchedString)
            edtSearch.setSelection(searchedString.length)
            search(viewModel, searchedString)
        }

        edtSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if(editable?.length!! > 0){
                    search(viewModel, editable.toString())
                    binding.appBarLayout.imgClose.visibility = View.VISIBLE
                }
            }

        })

        edtSearch.setOnEditorActionListener { view, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                val mFragmentManager = supportFragmentManager
                val mFragmentTransaction = mFragmentManager.beginTransaction()
                val mFragment = SearchFragment()
                //add search term to bundle
                val mBundle = Bundle()
                mBundle.putString(SearchFragment().SEARCH_TERM, edtSearch.text.toString())
                mFragment.arguments = mBundle
                //call fragment
                mFragmentTransaction.add(R.id.frameLayout, mFragment).commit()
                edtSearch.clearFocus()
                //set visibility GONE to layout of activity
                binding.mainLayout.visibility = View.GONE
                hideKeyboard(view)
                true
            } else {
                false
            }
        }

        clearEditText()

        //add divider for recycler view items
        binding.recyclerViewLayout.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun search(viewModel: SearchSuggestionViewModel, searchKeyword: String) {

        viewModel.searchKeyword = searchKeyword
        viewModel.getLiveDataObserver().observe(this, { result ->
            if(result != null ){
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

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clearEditText(){
        binding.appBarLayout.imgClose.setOnClickListener{
            edtSearch.text.clear()
        }
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