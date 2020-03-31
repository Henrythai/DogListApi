package com.example.jetpackapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.jetpackapp.model.DogBreed
import com.example.jetpackapp.model.DogDatabase
import com.example.jetpackapp.model.DogsApiService
import com.example.jetpackapp.util.NotificationHelper
import com.example.jetpackapp.util.SharePreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = "ListViewModel"
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private val prefHelp = SharePreferencesHelper(getApplication())

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelp.getUpdateTime()
        if (updateTime != null && updateTime != 0L && updateTime + refreshTime > System.nanoTime()) {
            fetchFromDB()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cacheRef =
            SharePreferencesHelper(getApplication()).getCacheDuration()?.toLongOrNull()
        cacheRef?.let {
            refreshTime = it.times(1000 * 1000 * 1000L)
        }

//        Toast.makeText(getApplication(), refreshTime.toString(), Toast.LENGTH_LONG).show()
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    fun fetchFromDB() {
        loading.value = true
        launch {
            Log.d(TAG, "less than 5 m, Retrive from DB")
            dogRetrived(DogDatabase(getApplication()).dogDao().getAllDogs())
        }

    }


    fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(t: List<DogBreed>) {
                        storeDogLocally(t)
                        Log.d(TAG, "more than 5 m, Retrive from API")
                        NotificationHelper(getApplication()).create()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun dogRetrived(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogLocally(dogList: List<DogBreed>) {
        launch {
            val dogDao = DogDatabase(context = getApplication()).dogDao()
            dogDao.deleteDogs()
            val idList = dogDao.insertAll(*dogList.toTypedArray())
            var index = 0
            while (index < idList.size) {
                dogList[index].uuid = idList[index].toInt()
                index++
            }

            dogRetrived(dogList)
            prefHelp.saveUpdateTime(System.nanoTime())
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()

    }
}