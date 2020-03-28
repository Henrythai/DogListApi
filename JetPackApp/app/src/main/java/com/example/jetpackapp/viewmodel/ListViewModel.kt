package com.example.jetpackapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackapp.model.DogBreed
import com.example.jetpackapp.model.DogsApiService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
//        val dog1 = DogBreed("1", "LuNa", "15 years", "Group", "bredFor", "null", "")
//        val dog2 = DogBreed("2", "KingKok", "20 years", "Group", "bredFor", "null", "")
//        val dog3 = DogBreed("3", "LongNen", "35 years", "Group", "bredFor", "null", "")
//        val DogList = arrayListOf<DogBreed>(dog1, dog2, dog3)
//
//        dogs.value = DogList
//        dogsLoadError.value = false
//        loading.value = false

        fetchFromRemote()

    }

    fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(t: List<DogBreed>) {
                        dogs.value = t
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()

    }
}