package com.example.blocked.local

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

    @Query("DELETE FROM `blocked numbers` WHERE number LIKE :number")
    fun deleteContact(number: String)
}