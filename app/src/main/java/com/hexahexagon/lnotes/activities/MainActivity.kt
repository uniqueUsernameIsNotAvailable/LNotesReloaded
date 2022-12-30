package com.hexahexagon.lnotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ActivityMainBinding
import com.hexahexagon.lnotes.dialogs.NewListDialog
import com.hexahexagon.lnotes.fragments.FragmentManager
import com.hexahexagon.lnotes.fragments.NoteFragment
import com.hexahexagon.lnotes.fragments.TodoListsFragment
import com.hexahexagon.lnotes.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(TodoListsFragment.newInstance(), this)
        setButtonNavListener()
    }

    private fun setButtonNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.todos -> {
                    FragmentManager.setFragment(TodoListsFragment.newInstance(), this)
                }
                R.id.create_item -> {
                    FragmentManager.currentFragment?.onClickNew()
                }

            }
            true
        }
    }

    override fun onClick(name: String) {
    }
}