package com.example.jetpackapp.viewmodel

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackapp.model.DogBreed
import com.example.jetpackapp.model.DogDatabase
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogBreed = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            val dog = dao.getDogById(uuid)
            dogBreed.value = dog
        }

    }


}