package com.jo.mvicleandemo.app_core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jo.mvicleandemo.app_core.data.local.room.AppDatabase.Companion.DATABASE_VERSION
import com.jo.mvicleandemo.auth_flow.data.local.AuthDao
import com.jo.mvicleandemo.auth_flow.domain.model.AppToken


@Database(
    entities = [
        AppToken::class
    ],
    version = DATABASE_VERSION,
//    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
/*@TypeConverters(
    SearchFiltersTypeConverter::class
)*/
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "CleanMviDemoDb"
        const val DATABASE_VERSION = 2
    }

    abstract fun authDao(): AuthDao

}