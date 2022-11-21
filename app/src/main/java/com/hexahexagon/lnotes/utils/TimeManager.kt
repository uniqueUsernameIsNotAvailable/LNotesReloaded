package com.hexahexagon.lnotes.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    fun getCurrentTime(): String {
        val format = SimpleDateFormat("dd.MM.yy - hh:mm", Locale.getDefault())
        return format.format(Calendar.getInstance().time)
    }
}