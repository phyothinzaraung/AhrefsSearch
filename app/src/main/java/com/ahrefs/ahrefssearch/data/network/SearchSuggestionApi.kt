package com.ahrefs.ahrefssearch.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchSuggestionApi {

    @GET("ac/")
    fun getSuggestions(@Query("q") searchKey: String, @Query("type") type: String): Call<List<Any>>

}