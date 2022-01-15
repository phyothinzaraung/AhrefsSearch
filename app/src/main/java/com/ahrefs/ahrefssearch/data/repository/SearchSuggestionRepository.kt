package com.ahrefs.ahrefssearch.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ahrefs.ahrefssearch.data.network.SearchSuggestionApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchSuggestionRepository @Inject constructor(private val api: SearchSuggestionApi) {

    fun getSearchSuggestion(query: String, type: String = "list", liveDataList: MutableLiveData<List<String>>) {
        val call: Call<List<String>> = api.getSuggestions(query, type)
        call?.enqueue(object : Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {

                if(response.isSuccessful) {
                    liveDataList.postValue(response.body())
                }else{
                    Log.v("error", "error occurs")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.v("error", t.localizedMessage)
                liveDataList.postValue(null)
            }

        })
    }

}
