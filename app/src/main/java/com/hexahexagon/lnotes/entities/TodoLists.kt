package com.hexahexagon.lnotes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo_list_names")
data class TodoLists(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "itemsCounter")
    val itemsCounter: Int,

    @ColumnInfo(name = "checkedItemsCounter")
    val checkedItemsCounter: Int,

    @ColumnInfo(name = "itemsIDs")
    val itemsIDs: String

) : Serializable
