package com.ahrefs.ahrefssearch.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ahrefs.ahrefssearch.data.network.SearchSuggestionApi
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchSuggestionRepository @Inject constructor(private val api: SearchSuggestionApi) {

    private val tag = "Search"

    fun getSearchSuggestion(query: String, type: String, liveDataList: MutableLiveData<List<String>>) {
        val call: Call<List<Any>> = api.getSuggestions(query, type)
        call.enqueue(object : Callback<List<Any>> {
            @SuppressLint("LongLogTag")
            override fun onResponse(
                call: Call<List<Any>>,
                response: Response<List<Any>>
            ) {
                if(response.isSuccessful) {
                    val data = JSONArray(response.body())
                    val dataArr: JSONArray = data[1] as JSONArray
                    val searchSuggestionList = arrayListOf<String>()
                    for (i in 0 until dataArr.length()){
                        searchSuggestionList.add(dataArr[i].toString())
                    }
                    liveDataList.postValue(searchSuggestionList)
                }else{
                    Log.e(tag, response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
                Log.e(tag, t.message.toString())
                //liveDataList.postValue(null)
            }

        })
    }

}
