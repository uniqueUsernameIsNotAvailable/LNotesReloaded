package com.hexahexagon.lnotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ActivityMainBinding
import com.hexahexagon.lnotes.dialogs.NewListDialog
import com.hexahexagon.lnotes.fragments.FragmentManager
import com.hexahexagon.lnotes.fragments.NoteFragment
import com.hexahexagon.lnotes.fragments.TodoListsFragment

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
                    Log.d("MyLog", "CLICKED_SETTINGS")
                }
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                    Log.d("MyLog", "CLICKED_NOTES")
                }
                R.id.todos -> {
                    FragmentManager.setFragment(TodoListsFragment.newInstance(), this)
                    Log.d("MyLog", "CLICKED_TODOS")
                }
                R.id.create_item -> {
                    FragmentManager.currentFragment?.onClickNew()
                    Log.d("MyLog", "CLICKED_NEW")
                }

            }
            true
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name $name")
    }
}