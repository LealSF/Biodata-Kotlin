package com.tugasakhirsemester.LabayElSulthanFatta

object Constanta {
    const val DB_NAME = "BIODATA"
    const val DB_VERSION = 1
    const val TABLE_NAME = "BIODATA_TABLE"
    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_IMAGE = "IMAGE"
    const val C_ALAMAT = "ALAMAT"
    const val C_EMAIL = "EMAIL"
    const val C_TELP = "TELP"
    const val C_TGLLAHIR = "TGLLAHIR"
    const val C_LHRTEMPAT = "LHRTEMPAT"
    const val C_KELAMIN = "KELAMIN"

    const val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + "TEXT,"
            + C_IMAGE + "TEXT,"
            + C_ALAMAT + "TEXT,"
            + C_EMAIL + "TEXT,"
            + C_TELP + "TEXT,"
            + C_TGLLAHIR + "TEXT,"
            + C_LHRTEMPAT + "TEXT,"
            + C_KELAMIN + "TEXT"
            + ")"
            )
}