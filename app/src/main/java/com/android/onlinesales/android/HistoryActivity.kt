package com.android.onlinesales.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.onlinesales.adapter.HistoryAdapter
import com.android.onlinesales.viewmodel.MainViewModel
import com.android.onlinesales.R

class HistoryActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        back = findViewById(R.id.imageViewBack)
        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        mainViewModel.getAllHistoryItems()?.observe(this, Observer { historyItems ->
            adapter.submitList(historyItems)
        })
    }
}
