package com.splitspendings.groupexpensesandroid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.splitspendings.groupexpensesandroid.database.dao.BalanceDao
import com.splitspendings.groupexpensesandroid.database.dao.CurrentAppUserDao
import com.splitspendings.groupexpensesandroid.database.dao.GroupDao
import com.splitspendings.groupexpensesandroid.database.dao.GroupMemberDao
import com.splitspendings.groupexpensesandroid.database.dao.PayoffDao
import com.splitspendings.groupexpensesandroid.database.dao.ShareDao
import com.splitspendings.groupexpensesandroid.database.dao.SpendingDao
import com.splitspendings.groupexpensesandroid.database.entity.BalanceEntity
import com.splitspendings.groupexpensesandroid.database.entity.CurrentAppUserEntity
import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity
import com.splitspendings.groupexpensesandroid.database.entity.GroupMemberEntity
import com.splitspendings.groupexpensesandroid.database.entity.PayoffEntity
import com.splitspendings.groupexpensesandroid.database.entity.ShareEntity
import com.splitspendings.groupexpensesandroid.database.entity.SpendingEntity

@Database(
    entities = [
        GroupEntity::class,
        SpendingEntity::class,
        BalanceEntity::class,
        GroupMemberEntity::class,
        ShareEntity::class,
        PayoffEntity::class,
        CurrentAppUserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GroupExpensesDatabase : RoomDatabase() {

    abstract val groupDao: GroupDao
    abstract val spendingDao: SpendingDao
    abstract val balanceDao: BalanceDao
    abstract val groupMemberDao: GroupMemberDao
    abstract val shareDao: ShareDao
    abstract val payoffDao: PayoffDao
    abstract val currentAppUserDao: CurrentAppUserDao

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