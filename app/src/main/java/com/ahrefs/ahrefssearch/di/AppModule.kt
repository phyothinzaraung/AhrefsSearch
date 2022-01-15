package com.ahrefs.ahrefssearch.di

import com.ahrefs.ahrefssearch.data.network.SearchSuggestionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://duckduckgo.com/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideSearchSuggestionApi(retrofit: Retrofit): SearchSuggestionApi =
    retrofit.create(SearchSuggestionApi::class.java)
}