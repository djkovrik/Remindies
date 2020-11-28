package com.sedsoftware.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

@Suppress("FunctionName")
fun RemindieDatabaseDriverFactory(): SqlDriver {
    val databasePath = File(System.getProperty("java.io.tmpdir"), "RemindieDatabase.db")
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
    RemindieDatabase.Schema.create(driver)
    return driver
}
