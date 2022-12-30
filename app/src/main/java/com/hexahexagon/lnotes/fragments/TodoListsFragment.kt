package com.hexahexagon.lnotes.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hexahexagon.lnotes.activities.MainApp
import com.hexahexagon.lnotes.activities.TodoActivity
import com.hexahexagon.lnotes.databinding.FragTodoListBinding
import com.hexahexagon.lnotes.db.MainViewModel
import com.hexahexagon.lnotes.db.TodoListAdapter
import com.hexahexagon.lnotes.dialogs.DeleteDialog
import com.hexahexagon.lnotes.dialogs.NewListDialog
import com.hexahexagon.lnotes.entities.TodoList
import com.hexahexagon.lnotes.utils.TimeManager


class TodoListsFragment : BaseFragment(), TodoListAdapter.Listener {
    private lateinit var binding: FragTodoListBinding
    private lateinit var adapter: TodoListAdapter


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val todoList = TodoList(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertTodoListName(todoList)
            }
        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = TodoListAdapter(this@TodoListsFragment)
        rcView.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allTodoListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    companion object {
        const val NEW_NOTE_KEY = "title_key"
        const val EDIT_STATE_KEY = "edit_state_key"

        @JvmStatic
        fun newInstance() = TodoListsFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteTodoList(id, true)
            }
        })
    }

    override fun editItem(todoNameItem: TodoList) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateTodoListName(todoNameItem.copy(name = name))
            }
        }, todoNameItem.name)
    }

    override fun onClickItem(todoNameItem: TodoList) {
        val i = Intent(activity, TodoActivity::class.java).apply {
            putExtra(TodoActivity.TODO_LIST, todoNameItem)
        }
        startActivity(i)
    }

//    override fun deleteItem(id: Int) {
//        mainViewModel.deleteNote(id)
//    }
//
//    override fun onClickItem(note: NoteItem) {
//        val intent = Intent(activity, NewNoteActivity::class.java).apply {
//            putExtra(NEW_NOTE_KEY, note)
//        }
//        editLauncher.launch(intent)
//    }
}

