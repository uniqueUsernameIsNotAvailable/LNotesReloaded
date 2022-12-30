package com.hexahexagon.lnotes.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.text.getSpans
import androidx.preference.PreferenceManager
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ActivityNewNoteBinding
import com.hexahexagon.lnotes.entities.NoteItem
import com.hexahexagon.lnotes.fragments.NoteFragment
import com.hexahexagon.lnotes.utils.HtmlManager
import com.hexahexagon.lnotes.utils.TimeManager
import com.hexahexagon.lnotes.utils.TouchListener
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        init()
        setTextSize()
        getNote()
        onClickColorPicker()
        //actionMenuCallback()
        //macrosParser
    }

    private fun init() {
        binding.colorPicker.setOnTouchListener(TouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY) as NoteItem
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
        edTitle.setText(note?.title)
        edDesc.setText(HtmlManager.getFromHtml(note?.data!!).trim())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveNote) {
            setMainResult()
        } else if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.bold) {
            setBoldForSelectedText()
        } else if (item.itemId == R.id.colors) {
            if (binding.colorPicker.isShown) {
                closeColorPicker()
            } else {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickColorPicker() = with(binding) {
        imgBtnRed.setOnClickListener { setColorForSelectedText(R.color.red) }
        imgBtnGreen.setOnClickListener { setColorForSelectedText(R.color.green) }
        imgBtnBlue.setOnClickListener { setColorForSelectedText(R.color.blue) }
        imgBtnPurple.setOnClickListener { setColorForSelectedText(R.color.purple) }
        imgBtnYellow.setOnClickListener { setColorForSelectedText(R.color.yellow) }
        imgBtnOrange.setOnClickListener { setColorForSelectedText(R.color.orange) }
    }

    private fun setBoldForSelectedText() = with(binding) {
        val beginPos = edDesc.selectionStart
        val endPos = edDesc.selectionEnd

        val styles = edDesc.text.getSpans(beginPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            edDesc.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
            edDesc.text.setSpan(boldStyle, beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            edDesc.text.trim()
            edDesc.setSelection(beginPos)
        }
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val beginPos = edDesc.selectionStart
        val endPos = edDesc.selectionEnd

        val styles = edDesc.text.getSpans(beginPos, endPos, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty()) edDesc.text.removeSpan(styles[0])

        edDesc.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this@NewNoteActivity, colorId
                )
            ), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        edDesc.text.trim()
        edDesc.setSelection(beginPos)

    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = edTitle.text.toString(), data = HtmlManager.toHtml(edDesc.text)
        )
    }

    private fun createNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDesc.text),
            TimeManager.getCurrentTime(),
            ""
        )
    }

    private fun actionBarSettings() {
        val temp = supportActionBar
        temp?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnimation = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnimation)
    }

    private fun closeColorPicker() {
        val openAnimation = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        binding.colorPicker.startAnimation(openAnimation)
    }


    private fun actionMenuCallback() {
        val actionCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }
        binding.edDesc.customSelectionActionModeCallback = actionCallback
    }

    private fun setTextSize() = with(binding) {
        edTitle.setTextSize(pref?.getString("size_heading", "20"))
        edDesc.setTextSize(pref?.getString("size_content", "14"))
    }

    private fun EditText.setTextSize(size: String?) {
        if (size != null) this.textSize = size.toFloat()
    }
}