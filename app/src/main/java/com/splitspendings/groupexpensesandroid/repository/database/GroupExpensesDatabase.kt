package com.splitspendings.groupexpensesandroid.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao
import com.splitspendings.groupexpensesandroid.repository.entity.Group

@Database(entities = [Group::class], version = 1, exportSchema = false)
abstract class GroupExpensesDatabase : RoomDatabase() {

    abstract val groupDao: GroupDao

    companion object {
        @Volatile
        private var INSTANCE: GroupExpensesDatabase? = null

        fun getInstance(context: Context): GroupExpensesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, GroupExpensesDatabase::class.java, "group_expenses_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}