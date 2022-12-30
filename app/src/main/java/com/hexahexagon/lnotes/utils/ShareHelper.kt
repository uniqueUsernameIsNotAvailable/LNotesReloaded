package com.hexahexagon.lnotes.utils

import android.content.Intent
import com.hexahexagon.lnotes.entities.TodoItem

object ShareHelper {
    fun shareTodoList(todoList: List<TodoItem>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply { putExtra(Intent.EXTRA_TEXT, shareContentMaker(todoList, listName)) }
        return intent
    }

    private fun shareContentMaker(todoList: List<TodoItem>, listName: String): String {
        val sBuilder = StringBuilder()
        sBuilder.append("$listName")
        sBuilder.append("\n")
        var counter = 0
        todoList.forEach {
            sBuilder.append("${++counter}) ${it.name} ${it.itemInfo}")
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }
}