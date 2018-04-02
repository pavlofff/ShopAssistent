package com.example.pavel.shopassistent

import android.app.ProgressDialog.show
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.example.pavel.shopassistent.R.id.*
import kotlinx.android.synthetic.main.activity_start.*





class MainActivity : AppCompatActivity() {

    private val textDimensions: Array<String> = arrayOf("кг.", "гр.", "шт.", "шт.")
    private val multiplyDimensions: Array<Int> = arrayOf(1, 100, 1, 10)
    private var currentSpinnerPosition = 0
    private lateinit var dataAdapter: DataAdapter
    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        clearItem.setOnClickListener({
            editPrice.text.clear()
            editCount.text.clear()
            editPrice.requestFocus()
        })

        editPrice.addTextChangedListener(CustomTextWatcher())
        editPrice.requestFocus()

        val arrayAdapter = ArrayAdapter.createFromResource(this, R.array.dimensions, R.layout.spinner_item)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
        spinner.adapter = arrayAdapter

        countColumn.text = String.format(getString(R.string.count_from_table), textDimensions[spinner.selectedItemPosition])

        dataAdapter = DataAdapter(multiplyDimensions[currentSpinnerPosition])
        dataAdapter.setHasStableIds(true)
        list.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            itemAnimator.apply {
                addDuration = 300
                changeDuration = 300
            }
            adapter = dataAdapter

        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, id: Long) {
                if (currentSpinnerPosition != position) {
                    countColumn.text = String.format(getString(R.string.count_from_table), textDimensions[position])
                    currentSpinnerPosition = position
                    dataAdapter.multiply = multiplyDimensions[currentSpinnerPosition]
                    dataAdapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        viewModel.getLiveDataItems().observe(this, Observer<List<QueryItem>> { dataAdapter.submitList(it as ArrayList<QueryItem>) })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteItem(dataAdapter.getItemId(position))
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(list)

        editCount.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!validate()) return@OnEditorActionListener true
                pressDone()
                editCount.text.clear()
                editPrice.apply {
                    text.clear()
                    requestFocus()
                }
            }
            return@OnEditorActionListener true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear -> {
                clearList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validate(): Boolean {
        val priceStr = editPrice.text.toString()
        val countStr = editCount.text.toString()
        var isValid = true
        if (priceStr.isEmpty() || countStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.message_error1), Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
                view.setBackgroundResource(R.drawable.toast_background)
                show()
            }
            isValid = false
        }
        return isValid
    }

    private fun clearList() {
        viewModel.deleteAll()
    }

    private fun pressDone() {
        val price = editPrice.text.toString().toDouble()
        val count = editCount.text.toString().toDouble()
        viewModel.insertItem(Item(price, count))
        list.smoothScrollToPosition(list.adapter.itemCount)
    }
}

class CustomTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val str = s.toString()
        val p = str.indexOf(".")
        if (p != -1) {
            val tmpStr = str.substring(p)
            if (tmpStr.length == 4) {
                s.delete(s.length - 1, s.length)
            }
        }
    }
}