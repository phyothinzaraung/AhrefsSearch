package com.ahrefs.ahrefssearch.ui

import androidx.lifecycle.*
import com.ahrefs.ahrefssearch.data.repository.SearchSuggestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SearchSuggestionViewModel @Inject constructor(
    private val repository: SearchSuggestionRepository
): ViewModel() {

    private var liveDataList: MutableLiveData<List<String>> = MutableLiveData()

    fun getLiveDataObserver(): MutableLiveData<List<String>> = liveDataList

    var searchKeyword: String = ""
    var job: Job? = null

    fun loadSearchSuggestionData() {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            repository.getSearchSuggestion(searchKeyword, "list", liveDataList)
        }

    }
}