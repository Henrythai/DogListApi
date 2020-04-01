package com.example.jetpackapp.data.network.service

import com.example.jetpackapp.data.network.model.DogBreed
import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}