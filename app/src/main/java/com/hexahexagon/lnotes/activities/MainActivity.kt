package com.hexahexagon.lnotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ActivityMainBinding
import com.hexahexagon.lnotes.fragments.FragmentManager
import com.hexahexagon.lnotes.fragments.NoteFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonNavListener()
    }

    private fun setButtonNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    Log.d("MyLog", "CLICKED_SETTINGS")
                }
                R.id.add_note -> {
                    FragmentManager.currentFragment?.onClickNew()
                    Log.d("MyLog", "CLICKED_NEW")
                }
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                    Log.d("MyLog", "CLICKED_NOTES")
                }
                R.id.todos -> {
                    Log.d("MyLog", "CLICKED_TODOS")
                }

            }
            true
        }
    }
}