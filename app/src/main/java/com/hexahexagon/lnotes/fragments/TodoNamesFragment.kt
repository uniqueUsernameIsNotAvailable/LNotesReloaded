package com.hexahexagon.lnotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hexahexagon.lnotes.activities.MainApp
import com.hexahexagon.lnotes.databinding.FragTodoListBinding
import com.hexahexagon.lnotes.db.MainViewModel
import com.hexahexagon.lnotes.db.TodoNameAdapter
import com.hexahexagon.lnotes.dialogs.DeleteDialog
import com.hexahexagon.lnotes.dialogs.NewListDialog
import com.hexahexagon.lnotes.entities.TodoLists
import com.hexahexagon.lnotes.utils.TimeManager


class TodoNamesFragment : BaseFragment(), TodoNameAdapter.Listener {
    private lateinit var binding: FragTodoListBinding
    private lateinit var adapter: TodoNameAdapter


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val todoLists = TodoLists(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertTodoListName(todoLists)
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
        adapter = TodoNameAdapter(this@TodoNamesFragment)
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
        fun newInstance() = TodoNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteTodoListName(id)
            }
        })
    }

    override fun editItem(todoNameItem: TodoLists) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateTodoListName(todoNameItem.copy(name = name))
            }
        }, todoNameItem.name)
    }

    override fun onClickItem(todoNameItem: TodoLists) {

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

