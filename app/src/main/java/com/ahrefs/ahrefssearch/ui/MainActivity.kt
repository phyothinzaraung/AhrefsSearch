package com.ahrefs.ahrefssearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahrefs.ahrefssearch.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding.apply {
//            edtSearch.setOnEditorActionListener { textView, actionId, event ->
//                edtSearch.clearFocus()
//                supportFragmentManager.beginTransaction().replace(R.id.container, SearchFragment())
//                    .commit()
//                edtSearch.visibility = GONE
//                true
//            }
//        }


    }
}