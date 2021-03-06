package com.tatuas.ghsv.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = UserDatabase.Constant.DB_VERSION)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: UserDatabase? = null

        fun getInstance(context: Context) = instance
                ?: synchronized(this) {
                    return@synchronized instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        UserDatabase.Constant.DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
    }

    object Constant {
        const val DB_NAME = "user.db"
        const val DB_VERSION = 1
    }

    abstract fun dao(): UserDao
}