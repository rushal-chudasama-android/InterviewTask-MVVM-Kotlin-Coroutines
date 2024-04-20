package com.mods.pink.house.interviewtask

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mods.pink.house.interviewtask.adapter.ListAdapter
import com.mods.pink.house.interviewtask.databinding.ActivityMainBinding
import com.mods.pink.house.interviewtask.view.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        adapter = ListAdapter(emptyList(), applicationContext)
        binding.rvList.layoutManager = GridLayoutManager(applicationContext, 3)
        binding.rvList.adapter = adapter

        viewModel.list.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                binding.imageView.visibility = View.GONE
                binding.tvNoData.visibility = View.GONE
            }
            adapter.addList(list)
        })

        viewModel.fetchData()

    }
}