package com.hexahexagon.lnotes.db

import androidx.lifecycle.*
import com.hexahexagon.lnotes.entities.NoteItem
import com.hexahexagon.lnotes.entities.TodoLists
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(dataBase: MainDataBase) : ViewModel() {
    val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allTodoListNames: LiveData<List<TodoLists>> = dao.getAllTodoListNames().asLiveData()

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertTodoListName(listName: TodoLists) = viewModelScope.launch {
        dao.insertTodoListName(listName)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateTodoListName(todoLists: TodoLists) = viewModelScope.launch {
        dao.updateListName(todoLists)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteTodoListName(id: Int) = viewModelScope.launch {
        dao.deleteTodoListName(id)
    }

    class MainViewModelFactory(val dataBase: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(dataBase) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}