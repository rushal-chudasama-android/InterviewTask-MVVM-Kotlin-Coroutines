package com.mods.pink.house.interviewtask.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mods.pink.house.interviewtask.api.RetrofitClient
import com.mods.pink.house.interviewtask.model.ApiResponseItem
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _list = MutableLiveData<List<ApiResponseItem>>()
    val list: LiveData<List<ApiResponseItem>> = _list

    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getData()
                _list.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}