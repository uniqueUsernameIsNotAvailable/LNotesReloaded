package com.hexahexagon.lnotes.db

import androidx.lifecycle.*
import com.hexahexagon.lnotes.entities.NoteItem
import com.hexahexagon.lnotes.entities.TodoItem
import com.hexahexagon.lnotes.entities.TodoList
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(dataBase: MainDataBase) : ViewModel() {
    val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allTodoListNames: LiveData<List<TodoList>> = dao.getAllTodoLists().asLiveData()

    fun getAllItemsFromList(listId:Int):LiveData<List<TodoItem>>{
        return dao.getAllTodoItems(listId).asLiveData()
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertTodoListName(listName: TodoList) = viewModelScope.launch {
        dao.insertTodoList(listName)
    }

    fun insertTodo(todo: TodoItem) = viewModelScope.launch {
        dao.insertTodo(todo)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateTodoListName(todoList: TodoList) = viewModelScope.launch {
        dao.updateTodoList(todoList)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteTodoListName(id: Int) = viewModelScope.launch {
        dao.deleteTodoList(id)
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