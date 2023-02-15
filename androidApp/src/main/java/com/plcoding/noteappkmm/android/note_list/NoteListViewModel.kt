package com.plcoding.noteappkmm.android.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.noteappkmm.domain.note.Note
import com.plcoding.noteappkmm.domain.note.NoteDataSource
import com.plcoding.noteappkmm.domain.note.SearchNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource
): ViewModel() {

    private val searchNotes = SearchNotes()

    private val notes = MutableStateFlow(emptyList<Note>())
    private val searchText = MutableStateFlow("")
    private val isSearchActive = MutableStateFlow(false)

    val state = combine(notes, searchText, isSearchActive) { notes, searchText, isSearchActive ->
        NoteListState(
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    fun loadNotes() {
        viewModelScope.launch {
            notes.value = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String) {
        searchText.value = text
    }

    fun onToggleSearch() {
        isSearchActive.update { !it }
        if(!isSearchActive.value) {
            searchText.value = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }
}