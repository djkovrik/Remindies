package com.sedsoftware.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

@Suppress("FunctionName")
actual fun RemindieTestDatabaseDriver(): SqlDriver {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    RemindieDatabase.Schema.create(driver)
    return driver
}
