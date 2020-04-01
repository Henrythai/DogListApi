package com.example.jetpackapp.ui.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.jetpackapp.data.network.model.DogBreed
import com.example.jetpackapp.data.db.DogDatabase
import com.example.jetpackapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

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