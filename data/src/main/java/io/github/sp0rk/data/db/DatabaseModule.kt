package io.github.sp0rk.data.db

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun databaseModule() = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            LocalDatabase::class.java,
            DB_FILE_NAME
        ).build()
    }
}

private const val DB_FILE_NAME = "bitask.db"
