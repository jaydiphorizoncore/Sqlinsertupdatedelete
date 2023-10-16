package com.example.sqliteinsertupdatedelete

import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView.OnQueryTextListener

class MainActivity : AppCompatActivity() {

    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var adapter: SimpleCursorAdapter

    lateinit var edName: EditText
    lateinit var edCity: EditText
    lateinit var btnInsert: Button
    lateinit var btnFirst: Button
    lateinit var btnNext: Button
    lateinit var btnPrevious: Button
    lateinit var btnLast: Button
    lateinit var btnUpdate: Button
    lateinit var btnDelete: Button
    lateinit var btnShowAll: Button
    lateinit var searchView: androidx.appcompat.widget.SearchView
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        var helper = MyHelper(applicationContext)
        db = helper.readableDatabase
        cursor = db.rawQuery("SELECT * FROM Student ", null)

        adapter = SimpleCursorAdapter(
            applicationContext,
            android.R.layout.simple_expandable_list_item_2,
            cursor,
            arrayOf("NAME", "CITY"),
            intArrayOf(android.R.id.text1, android.R.id.text2),
            0
        )
        listView.adapter = adapter

        registerForContextMenu(listView)

        //Insert Data
        btnInsert.setOnClickListener {

            if (edName.text.trim() != "") {

                helper.insertData(edName.text.toString(), edCity.text.toString())
                cursor.requery()
                edName.setText("")
                edCity.setText("")
                edName.requestFocus()
                adapter.notifyDataSetChanged()

            }

        }
        //Update Data
        btnUpdate.setOnClickListener {

            val newName = edName.text.toString()
            val newName2 = edCity.text.toString()
            val no = cursor.getString(0)

            helper.updateData(no, newName, newName2)
            cursor.requery()
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Update Record")
            alertDialog.setMessage("Record Update Successfully...!")
            alertDialog.setPositiveButton("OK",
                DialogInterface.OnClickListener { _, _ ->

                    if (cursor.moveToFirst()) {
                        edName.setText(cursor.getString(1))
                        edCity.setText(cursor.getString(2))
                    }
                })
            alertDialog.show()

        }

        //Delete Data

        btnDelete.setOnClickListener {
            helper.DeleteData(cursor.getString(0))
            if (cursor.moveToPosition(0)) {
                cursor.requery()
                adapter.notifyDataSetChanged()

                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Delete Record")
                alertDialog.setMessage("Record Delete Successfully...!")
                alertDialog.setPositiveButton("OK",
                    DialogInterface.OnClickListener { _, _ ->
                        if (cursor.moveToFirst()) {
                            edName.setText(cursor.getString(1))
                            edCity.setText(cursor.getString(2))
                        } else {
                            edName.setText(R.string.dataNOtFound)
                            edCity.setText(R.string.dataNOtFound)
                        }
                    })
                alertDialog.show()
            }
        }
        btnShowAll.setOnClickListener {

            adapter.notifyDataSetChanged()
            searchView.isIconified = true
            searchView.queryHint = "Search among ${cursor.count} Record"

            searchView.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE


        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                cursor = db.rawQuery(
                    "SELECT * FROM Student WHERE NAME LIKE '%${newText}%' OR CITY LIKE '%${newText}%'",
                    null
                )
                adapter.changeCursor(cursor)
                return false
            }

        })


        //First  Data

        btnFirst.setOnClickListener {
            if (cursor.moveToFirst()) {
                edName.setText(cursor.getString(1))
                edCity.setText(cursor.getString(2))
            } else {
                Toast.makeText(this, "Not Record In DataBase ", Toast.LENGTH_LONG).show()
            }
        }

        // Data Data
        btnLast.setOnClickListener {
            // rs.requery()
            if (cursor.moveToLast()) {
                edName.setText(cursor.getString(1))
                edCity.setText(cursor.getString(2))
            }
        }

        //Next Data

        btnNext.setOnClickListener {
            if (cursor.moveToNext()) {
                edName.setText(cursor.getString(1))
                edCity.setText(cursor.getString(2))
            } else {
                Toast.makeText(this, "Already last data", Toast.LENGTH_LONG).show()
            }
        }

        //Previous Data

        btnPrevious.setOnClickListener {
            if (cursor.moveToPrevious()) {
                edName.setText(cursor.getString(1))
                edCity.setText(cursor.getString(2))
            }
        }
    }

    private fun initViews() {
        edName = findViewById(R.id.edName)
        edCity = findViewById(R.id.edCity)
        btnInsert = findViewById(R.id.button)
        btnFirst = findViewById(R.id.btnFirst)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnLast = findViewById(R.id.btnLast)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnShowAll = findViewById(R.id.btnShowAll)
        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(101, 11, 1, "DELETE")
        menu?.setHeaderTitle("Removing Data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 11) {
            db.delete("Student", "_id = ?", arrayOf(cursor.getString(0)))
            cursor.requery()
            adapter.notifyDataSetChanged()
            searchView.queryHint = "Search among ${cursor.count} Record"
        }
        return super.onContextItemSelected(item)

    }
}