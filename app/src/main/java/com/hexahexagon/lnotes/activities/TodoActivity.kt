package com.hexahexagon.lnotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ActivityTodoBinding
import com.hexahexagon.lnotes.db.MainViewModel
import com.hexahexagon.lnotes.db.TodoItemAdapter
import com.hexahexagon.lnotes.db.TodoListAdapter
import com.hexahexagon.lnotes.entities.TodoItem
import com.hexahexagon.lnotes.entities.TodoList


class TodoActivity : AppCompatActivity(), TodoItemAdapter.Listener {
    private lateinit var binding: ActivityTodoBinding
    private var todoList: TodoList? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: TodoItemAdapter? = null

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

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_item)
            addNewTodoItem()

        return super.onOptionsItemSelected(item)
    }

    private fun addNewTodoItem() {
        if (edItem?.text.toString().isEmpty()) return

        val item = TodoItem(
            null,
            edItem?.text.toString(),
            null,
            0,
            todoList?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertTodo(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(todoList?.id!!).observe(this, {
            adapter?.submitList(it)
        })
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
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
                return true
            }

        }
    }

    private fun init() {
        todoList = intent.getSerializableExtra(TODO_LIST) as TodoList
        binding.tvTest.text = todoList?.name
    }

    companion object {
        const val TODO_LIST = "todo_list_names"
    }

    override fun deleteItem(id: Int) {

    }

    override fun editItem(todoNameItem: TodoList) {

    }

    override fun onClickItem(todoNameItem: TodoList) {

    }
}