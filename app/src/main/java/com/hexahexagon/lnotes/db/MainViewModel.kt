package com.hexahexagon.lnotes.db

import androidx.lifecycle.*
import com.hexahexagon.lnotes.entities.LibraryItem
import com.hexahexagon.lnotes.entities.NoteItem
import com.hexahexagon.lnotes.entities.TodoItem
import com.hexahexagon.lnotes.entities.TodoList
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(dataBase: MainDataBase) : ViewModel() {
    val dao = dataBase.getDao()
    val libItems = MutableLiveData<List<LibraryItem>>()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allTodoListNames: LiveData<List<TodoList>> = dao.getAllTodoLists().asLiveData()

    fun getAllItemsFromList(listId: Int): LiveData<List<TodoItem>> {
        return dao.getAllTodoItems(listId).asLiveData()
    }

    fun getLibItems(name: String) = viewModelScope.launch {
        libItems.postValue(dao.getAllLibItems(name))
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertTodoListName(listName: TodoList) = viewModelScope.launch {
        dao.insertTodoList(listName)
    }

    fun insertTodo(todo: TodoItem) = viewModelScope.launch {
        dao.insertTodo(todo)
        if (!doesLibItemExist(todo.name)) dao.insertLibItem(LibraryItem(null, todo.name))
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateTodo(todo: TodoItem) = viewModelScope.launch {
        dao.updateTodo(todo)
    }

    fun updateTodoListName(todoList: TodoList) = viewModelScope.launch {
        dao.updateTodoList(todoList)
    }

    fun updateLibItem(libraryItem: LibraryItem) = viewModelScope.launch {
        dao.updateLibItem(libraryItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteTodoList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if (deleteList) dao.deleteTodoList(id)
        dao.deleteTodoByListId(id)
    }

    fun deleteLibItem(id: Int) = viewModelScope.launch {
        dao.deleteLibItem(id)
    }

    suspend fun doesLibItemExist(name: String): Boolean {
        return dao.getAllLibItems(name).isNotEmpty()
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