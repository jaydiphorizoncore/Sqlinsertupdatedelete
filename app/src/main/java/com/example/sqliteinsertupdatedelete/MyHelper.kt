package com.example.sqliteinsertupdatedelete

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context) : SQLiteOpenHelper(context, "School", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(" CREATE TABLE Student(_id integer primary key autoincrement,NAME TEXT,CITY TEXT)")
        /*db?.execSQL("INSERT INTO Student(NAME,CITY) VALUES ('Akash','Halvad')")
        db?.execSQL("INSERT INTO Student(NAME,CITY) VALUES ('Akash','Halvad')")*/
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
    //method to insert data

    fun insertData(name: String, city: String): String {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("NAME", name)
        cv.put("CITY", city)

        val success = db.insert("Student", null, cv).toString()
        //db.close()
        return success

    }

    fun updateData(no: String, name1: String, city1: String): Any {


        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM Student ", null)

        val cv = ContentValues()
        cv.put("NAME", name1)
        cv.put("CITY", city1)
        val data = db.update("Student", cv, "_id = ? ", arrayOf(no))
        rs.requery()
        return data

    }

    fun DeleteData(n: String): Any {
        val db = this.writableDatabase
        //val rs = db.rawQuery("SELECT * FROM Student ", null)
        val data = db.delete("Student", "_id = ? ", arrayOf(n))
        // rs.requery()
        return data

    }

}

