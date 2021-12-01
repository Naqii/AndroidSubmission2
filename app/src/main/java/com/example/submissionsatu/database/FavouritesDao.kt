package com.example.submissionsatu.database
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouritesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favourites: Favourites)

    @Delete
    fun delete(favourites: Favourites)

    @Query("SELECT * from favourites ORDER BY username ASC")
    fun getAllFav(): LiveData<List<Favourites>>
}