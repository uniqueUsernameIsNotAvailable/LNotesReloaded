package com.hexahexagon.lnotes.entities

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.DeleteTable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes_list_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "itemInfo")
    val itemInfo: String?,

    @ColumnInfo(name = "itemChecked")
    val itemChecked: Int = 0,

    @ColumnInfo(name = "listID")
    val listId: Int,

    @ColumnInfo(name = "itemType")
    val itemType: Int = 0

)
