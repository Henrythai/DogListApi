package com.example.jetpackapp.ui.list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.jetpackapp.data.network.model.DogBreed
import com.example.jetpackapp.data.db.DogDatabase
import com.example.jetpackapp.data.network.service.DogsApiService
import com.example.jetpackapp.ui.base.BaseViewModel
import com.example.jetpackapp.util.NotificationHelper
import com.example.jetpackapp.util.SharePreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application, dogsApiService: DogsApiService) :
    BaseViewModel(application) {

    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private val prefHelp = SharePreferencesHelper(getApplication())

    private val dogsService: DogsApiService = dogsApiService

    private val disposable = CompositeDisposable()

    var dogs = MutableLiveData<List<DogBreed>>()
    var dogsLoadError = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()


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
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    fun fetchFromDB() {
        loading.value = true
        launch {
            Toast.makeText(getApplication(), "Get data from DB", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(
                            getApplication(),
                            "Get data from Rest API",
                            Toast.LENGTH_SHORT
                        ).show()
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
            val dogDao = DogDatabase(getApplication()).dogDao()
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