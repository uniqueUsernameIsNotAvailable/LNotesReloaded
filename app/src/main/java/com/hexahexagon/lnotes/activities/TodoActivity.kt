package com.hexahexagon.lnotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ActivityTodoBinding
import com.hexahexagon.lnotes.db.MainViewModel
import com.hexahexagon.lnotes.db.TodoItemAdapter
import com.hexahexagon.lnotes.dialogs.UpdateTodoDialog
import com.hexahexagon.lnotes.entities.LibraryItem
import com.hexahexagon.lnotes.entities.TodoItem
import com.hexahexagon.lnotes.entities.TodoList
import com.hexahexagon.lnotes.utils.ShareHelper


class TodoActivity : AppCompatActivity(), TodoItemAdapter.Listener {
    private lateinit var binding: ActivityTodoBinding
    private var todoList: TodoList? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: TodoItemAdapter? = null
    private lateinit var textWatcher: TextWatcher

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTodoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)!!
        edItem = newItem.actionView.findViewById(R.id.edNewTodoItem) as EditText
        newItem.setOnActionExpandListener(expandActionMenu())
        saveItem.isVisible = false
        textWatcher = textWatcher()

        return true
    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.getLibItems("%$p0%")
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addNewTodoItem(edItem?.text.toString())
            }
            R.id.delete_list -> {
                mainViewModel.deleteTodoList(todoList?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteTodoList(todoList?.id!!, false)
            }
            R.id.share_list -> {
                startActivity(
                    Intent.createChooser(
                        ShareHelper.shareTodoList(
                            adapter?.currentList!!,
                            todoList?.name!!
                        ), "Share with"
                    )
                )
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addNewTodoItem(name: String) {
        if (name.isEmpty()) return

        val item = TodoItem(
            null,
            name,
            "",
            false,
            todoList?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertTodo(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(todoList?.id!!).observe(this) {
            adapter?.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else
                View.GONE
        }
    }

    private fun libItemObserver() {
        mainViewModel.libItems.observe(this) {
            val tmpTodo = ArrayList<TodoItem>()
            it.forEach { item ->
                val todoItem = TodoItem(item.id, item.name, "", false, 0, 1)
                tmpTodo.add(todoItem)
            }
            adapter?.submitList(tmpTodo)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else
                View.GONE
        }
    }

    private fun initRcView() = with(binding) {
        adapter = TodoItemAdapter(this@TodoActivity)
        rcView.layoutManager = LinearLayoutManager(this@TodoActivity)
        rcView.adapter = adapter

    }

    private fun expandActionMenu(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libItemObserver()
                mainViewModel.getAllItemsFromList(todoList?.id!!).removeObservers(this@TodoActivity)
                mainViewModel.getLibItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libItems.removeObservers(this@TodoActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }

        }
    }

    private fun init() {
        todoList = intent.getSerializableExtra(TODO_LIST) as TodoList
    }

    companion object {
        const val TODO_LIST = "todo_list_names"
    }


    override fun onClickItem(todoItem: TodoItem, state: Int) {
        when (state) {
            TodoItemAdapter.CHECK -> mainViewModel.updateTodo(todoItem)
            TodoItemAdapter.EDIT -> editTodo(todoItem)
            TodoItemAdapter.EDIT_LIB_ITEM -> editLibItem(todoItem)
            TodoItemAdapter.DELETE_LIB_ITEM -> {
                mainViewModel.deleteLibItem(todoItem.id!!)
                mainViewModel.getLibItems("%${edItem?.text.toString()}%")
            }
            TodoItemAdapter.INSERT -> addNewTodoItem(todoItem.name)
        }

    }

    private fun editTodo(item: TodoItem) {
        UpdateTodoDialog.showDialog(this, item, object : UpdateTodoDialog.Listener {
            override fun onClick(item: TodoItem) {
                mainViewModel.updateTodo(item)
            }

        })
    }

    private fun editLibItem(item: TodoItem) {
        UpdateTodoDialog.showDialog(this, item, object : UpdateTodoDialog.Listener {
            override fun onClick(item: TodoItem) {
                mainViewModel.updateLibItem(LibraryItem(item.id, item.name))
                mainViewModel.getLibItems("%${edItem?.text.toString()}%")
            }

        })
    }

    private fun saveChecksCount() {
        var checkedCounter = 0
        adapter?.currentList?.forEach {
            if (it.itemChecked) checkedCounter++
        }
        val tmpTodoList = todoList?.copy(
            itemsCounter = adapter?.itemCount!!,
            checkedItemsCounter = checkedCounter
        )
        mainViewModel.updateTodoListName(tmpTodoList!!)
    }

    override fun onBackPressed() {
        saveChecksCount()
        super.onBackPressed()
    }
}