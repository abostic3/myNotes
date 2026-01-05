package com.consumer.notesapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE isSecret = 0 ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE isSecret = 1 ORDER BY updatedAt DESC")
    fun getSecretNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id LIMIT 1")
    fun getById(id: Long): Note?

    @Insert
    fun insert(note: Note): Long

    @Update
    fun update(note: Note): Int

    @Delete
    fun delete(note: Note): Int
}
