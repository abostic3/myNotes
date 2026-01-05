package com.consumer.notesapp

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(context: Context) {
    private val dao = NoteDatabase.getInstance(context).noteDao()
    fun allNotes(): Flow<List<Note>> = dao.getAllNotes()
    fun secretNotes(): Flow<List<Note>> = dao.getSecretNotes()
    suspend fun getById(id: Long): Note? = withContext(Dispatchers.IO) { dao.getById(id) }
    // Blocking variant used to avoid coroutines on lifecycle activation paths
    fun getByIdBlocking(id: Long): Note? = dao.getById(id)
    suspend fun insert(note: Note) = withContext(Dispatchers.IO) { dao.insert(note) }
    suspend fun update(note: Note) = withContext(Dispatchers.IO) { dao.update(note) }
    suspend fun delete(note: Note) = withContext(Dispatchers.IO) { dao.delete(note) }
}
