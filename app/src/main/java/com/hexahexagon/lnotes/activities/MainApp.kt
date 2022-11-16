package com.hexahexagon.lnotes.activities

import android.app.Application
import com.hexahexagon.lnotes.db.MainDataBase

class MainApp:Application() {
    val database by lazy {MainDataBase.getDataBase(this)}
}