package com.hexahexagon.lnotes.fragments

import androidx.appcompat.app.AppCompatActivity
import com.hexahexagon.lnotes.R

object FragmentManager {
    var currentFragment: BaseFragment? = null

    fun setFragment(newFragment: BaseFragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.placeholder, newFragment)
        transaction.commit()
        currentFragment = newFragment
    }
}