package com.hexahexagon.lnotes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hexahexagon.lnotes.entities.NoteItem
import com.hexahexagon.lnotes.entities.TodoItem
import com.hexahexagon.lnotes.entities.TodoList
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertTodo(todo: TodoItem)

    @Insert
    suspend fun insertTodoList(name: TodoList)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateTodo(todo: TodoItem)

    @Update
    suspend fun updateTodoList(todoList: TodoList)

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM todo_list_names WHERE id IS :id")
    suspend fun deleteTodoList(id: Int)

    @Query("DELETE FROM notes_list_items WHERE listID LIKE :listId ")
    suspend fun deleteTodoByListId(listId: Int)

    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM todo_list_names")
    fun getAllTodoLists(): Flow<List<TodoList>>

    @Query("SELECT * FROM notes_list_items WHERE listID LIKE :listId ")
    fun getAllTodoItems(listId: Int): Flow<List<TodoItem>>
}