package com.example.blocked

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Blocked Numbers")
data class ContactModel(
    @PrimaryKey(autoGenerate = false)
    val number:String?
) {
}