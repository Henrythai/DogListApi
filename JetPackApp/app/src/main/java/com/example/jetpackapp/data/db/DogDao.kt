package com.example.jetpackapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jetpackapp.data.network.model.DogBreed

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("Select * From DogBreed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("Select * From DogBreed Where uuid= :uuid")
    suspend fun getDogById(uuid: Int): DogBreed

    @Query("Delete from DogBreed")
    suspend fun deleteDogs()
}