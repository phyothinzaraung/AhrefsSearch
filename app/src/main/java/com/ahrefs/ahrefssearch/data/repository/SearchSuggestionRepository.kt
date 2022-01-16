package com.ahrefs.ahrefssearch.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ahrefs.ahrefssearch.data.network.SearchSuggestionApi
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchSuggestionRepository @Inject constructor(private val api: SearchSuggestionApi) {

    fun getSearchSuggestion(query: String, type: String, liveDataList: MutableLiveData<List<String>>) {
        val call: Call<List<Any>> = api.getSuggestions(query, type)
        call.enqueue(object : Callback<List<Any>> {
            override fun onResponse(
                call: Call<List<Any>>,
                response: Response<List<Any>>
            ) {
                if(response.isSuccessful) {
                    val data = JSONArray(response.body())
                    val searchSuggestionList = arrayListOf<String>()
                    for(i in 0 until data.length()){
                        try {
                            val arrayText = JSONArray(data[i].toString())
                            for (j in 0 until arrayText.length()){
                                Log.d("Result", arrayText[j].toString())
                                searchSuggestionList.add(arrayText[j].toString())
                            }
                            liveDataList.postValue(searchSuggestionList)
                        }catch (exception: JSONException){
                            Log.d("searchTerm", data[i].toString())
                        }
                    }
                }else{
                    Log.v("error", "error occurs")
                }
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
                liveDataList.postValue(null)
            }

        })
    }

}
