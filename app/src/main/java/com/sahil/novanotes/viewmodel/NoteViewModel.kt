package com.sahil.novanotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahil.novanotes.data.Note
import com.sahil.novanotes.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel (private  val repository: NoteRepository): ViewModel(){

    val allNotes = repository.allNotes

    fun insertNote(note: Note){
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            repository.delete((note))
        }
    }

    suspend fun getNoteById(id: Int): Note? {
        return repository.getNoteById(id)
    }

}
