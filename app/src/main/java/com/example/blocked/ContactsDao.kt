package com.example.blocked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: ContactModel)

    @Query("SELECT * FROM `blocked numbers`")
    fun query(): Observable<List<ContactModel>>
}