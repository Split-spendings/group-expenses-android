package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.BalanceEntity

@Dao
interface BalanceDao {

    @Query("SELECT * from balance where id = :id")
    fun getLive(id: Long): LiveData<BalanceEntity>

    @Query("SELECT * FROM balance")
    fun getAllLive(): LiveData<List<BalanceEntity>>

    @Query("SELECT * FROM balance where groupId = :groupId")
    fun getByGroupIdLive(groupId: Long): LiveData<List<BalanceEntity>>

    @Insert
    suspend fun insert(balance: BalanceEntity): Long

    @Insert
    suspend fun insertAll(balancesList: List<BalanceEntity>)

    @Update
    suspend fun update(balance: BalanceEntity): Int

    @Query("SELECT * from balance where id = :id")
    suspend fun get(id: Long): BalanceEntity?

    @Query("SELECT * FROM balance")
    suspend fun getAll(): List<BalanceEntity>

    @Query("DELETE FROM balance")
    suspend fun clear()

    @Query("DELETE FROM balance where groupId = :groupId")
    suspend fun deleteByGroupId(groupId: Long)

    @Query("SELECT * FROM balance where groupId = :groupId")
    suspend fun getByGroupId(groupId: Long): List<BalanceEntity>
}
