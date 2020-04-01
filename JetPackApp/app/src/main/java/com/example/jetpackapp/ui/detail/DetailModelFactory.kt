package com.example.jetpackapp.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class DetailModelFactory(val application: Application, val detailListeners: DetailListeners) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application, detailListeners) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}