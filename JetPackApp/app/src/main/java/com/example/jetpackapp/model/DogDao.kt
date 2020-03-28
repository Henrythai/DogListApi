package com.example.jetpackapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("Select * From DogBreed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("Select * From DogBreed Where breed_id= :uuid")
    suspend fun getDogById(uuid: Int): DogBreed

    @Query("Delete from DogBreed")
    suspend fun deleteDogs()
}