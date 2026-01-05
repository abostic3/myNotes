package com.consumer.notesapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = NoteRepository(application)
    val notes: LiveData<List<Note>> = repo.allNotes().asLiveData()
    val secretNotes: LiveData<List<Note>> = repo.secretNotes().asLiveData()

    fun getNoteLive(id: Long): LiveData<Note?> {
        val data = MutableLiveData<Note?>(null)
        // Load on background thread without using coroutines to avoid coroutine runtime issues during lifecycle activation
        Thread {
            val note = repo.getByIdBlocking(id)
            data.postValue(note)
        }.start()
        return data
    }

    fun insert(note: Note) = viewModelScope.launch { repo.insert(note) }
    suspend fun insertAndReturnId(note: Note): Long = repo.insert(note)
    fun update(note: Note) = viewModelScope.launch { repo.update(note) }
    fun delete(note: Note) = viewModelScope.launch { repo.delete(note) }
}
