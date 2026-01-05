package com.consumer.notesapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var title: String? = null,
    var content: String? = null,
    var isSecret: Boolean = false,
    var createdAt: Long = 0L,
    var updatedAt: Long = 0L
)
