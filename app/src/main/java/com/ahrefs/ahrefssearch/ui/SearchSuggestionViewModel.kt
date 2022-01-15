package com.ahrefs.ahrefssearch.ui

import androidx.lifecycle.*
import com.ahrefs.ahrefssearch.data.repository.SearchSuggestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class SearchSuggestionViewModel @Inject constructor(
    private val repository: SearchSuggestionRepository
): ViewModel() {

    var liveDataList: MutableLiveData<List<String>> = MutableLiveData()

    fun getLiveDataObserver(): MutableLiveData<List<String>> = liveDataList

    fun loadSearchSuggestionData() = repository.getSearchSuggestion("a", "list", liveDataList)
}