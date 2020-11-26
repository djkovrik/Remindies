package com.sedsoftware.common.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

@Suppress("FunctionName")
fun RemindieDatabaseDriverFactory(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = RemindieDatabase.Schema,
        context = context,
        name = "RemindieDatabase.db"
    )
