package com.example.jetpackapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackapp.model.DogBreed

class ListViewModel : ViewModel() {
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val dog1 = DogBreed("1", "LuNa", "15 years", "Group", "bredFor", "null", "")
        val dog2 = DogBreed("2", "KingKok", "20 years", "Group", "bredFor", "null", "")
        val dog3 = DogBreed("3", "LongNen", "35 years", "Group", "bredFor", "null", "")
        val DogList = arrayListOf<DogBreed>(dog1, dog2, dog3)

        dogs.value = DogList
        dogsLoadError.value = false
        loading.value = false
    }
}