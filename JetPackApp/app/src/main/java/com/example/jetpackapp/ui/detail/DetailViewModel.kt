package com.example.jetpackapp.ui.detail

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.jetpackapp.data.network.model.DogBreed
import com.example.jetpackapp.data.db.DogDatabase
import com.example.jetpackapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class DetailViewModel(application: Application, val detailListeners: DetailListeners) :
    BaseViewModel(application) {

    val dogBreed = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            val dog = dao.getDogById(uuid)
            dogBreed.value = dog
        }

    }

    fun shareWithOtherApp() {
        dogBreed.value?.let {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed")
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "${it.dogBreed} bred for ${it.bredFor}"
            )
            intent.putExtra(Intent.EXTRA_STREAM, it.imageUrl)
            detailListeners.startActivityCreateChooser(intent)
        }

    }


}