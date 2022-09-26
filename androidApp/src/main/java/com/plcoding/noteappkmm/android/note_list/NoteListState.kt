package com.plcoding.noteappkmm.android.note_list

import com.plcoding.noteappkmm.domain.note.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false
)
