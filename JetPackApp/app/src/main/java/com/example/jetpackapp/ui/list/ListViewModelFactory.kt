package com.example.jetpackapp.ui.list

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackapp.data.network.service.DogsApiService

class ListViewModelFactory(val application: Application, val dogsApiService: DogsApiService) :
    ViewModelProvider.Factory {

    @NonNull
    override fun <T : ViewModel?> create(@NonNull modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(application, dogsApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}