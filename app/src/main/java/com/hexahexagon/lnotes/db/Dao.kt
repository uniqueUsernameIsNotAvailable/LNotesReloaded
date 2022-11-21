package com.hexahexagon.lnotes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hexahexagon.lnotes.entities.NoteItem
import com.hexahexagon.lnotes.entities.TodoLists
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertTodoListName(name: TodoLists)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateListName(todoLists: TodoLists)

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM todo_list_names WHERE id IS :id")
    suspend fun deleteTodoListName(id: Int)

    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM todo_list_names")
    fun getAllTodoListNames(): Flow<List<TodoLists>>
}