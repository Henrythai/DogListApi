package com.example.jetpackapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackapp.model.DogBreed

class DetailViewModel : ViewModel() {

    val dogBreed = MutableLiveData<DogBreed>()

    fun fetch() {
        val dog1 = DogBreed("1", "LuNa", "15 years", "Group", "bredFor", "null", "")
        dogBreed.value = dog1
    }
}