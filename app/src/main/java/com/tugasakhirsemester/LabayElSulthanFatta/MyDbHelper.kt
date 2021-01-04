package com.tugasakhirsemester.LabayElSulthanFatta

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context?):SQLiteOpenHelper(
        context,
        Constanta.DB_NAME,
        null,
        Constanta.DB_VERSION
) {
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(Constanta.CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLEIF EXISTS " + Constanta.TABLE_NAME)
        onCreate(p0)
    }

    fun insertRecord(
            name:String?,
            image:String?,
            alamat:String?,
            email:String?,
            telp:String?,
            lahir:String?,
            tempat:String?,
            kelamin:String?
    ):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constanta.C_NAME, name)
        values.put(Constanta.C_IMAGE, image)
        values.put(Constanta.C_ALAMAT, alamat)
        values.put(Constanta.C_EMAIL, email)
        values.put(Constanta.C_TELP, telp)
        values.put(Constanta.C_TGLLAHIR, lahir)
        values.put(Constanta.C_LHRTEMPAT, tempat)
        values.put(Constanta.C_KELAMIN, kelamin)

        val id = db.insert(Constanta.TABLE_NAME, null, values)
        db.close()
        return id
    }
}