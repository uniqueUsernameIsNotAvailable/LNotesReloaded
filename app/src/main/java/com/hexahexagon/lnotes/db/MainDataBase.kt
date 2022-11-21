package com.hexahexagon.lnotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hexahexagon.lnotes.entities.CheckLNotesItems
import com.hexahexagon.lnotes.entities.TodoLists
import com.hexahexagon.lnotes.entities.LibraryItem
import com.hexahexagon.lnotes.entities.NoteItem

@Database(
    entities = [CheckLNotesItems::class, TodoLists::class,
        LibraryItem::class, NoteItem::class], version = 1
)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "lnotes_list.db"
                ).build()
                instance
            }
        }
    }
}