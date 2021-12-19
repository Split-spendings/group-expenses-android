package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.SpendingEntity

@Dao
interface SpendingDao {

    @Insert
    suspend fun insert(spending: SpendingEntity): Long

    @Insert
    suspend fun insertAll(spendingsList: List<SpendingEntity>)

    @Update
    suspend fun update(group: SpendingEntity): Int

    @Query("SELECT * from spending where id = :id")
    suspend fun get(id: Long): SpendingEntity?

    @Query("SELECT * from spending where id = :id")
    fun getLive(id: Long): LiveData<SpendingEntity>

    @Query("SELECT * FROM spending")
    suspend fun getAll(): List<SpendingEntity>

    @Query("SELECT * FROM spending")
    fun getAllLive(): LiveData<List<SpendingEntity>>

    @Query("DELETE FROM spending")
    suspend fun clear()

    @Query("DELETE FROM spending where groupId = :groupId")
    suspend fun deleteByGroupId(groupId: Long)

    @Query("SELECT * FROM spending where groupId = :groupId")
    suspend fun getByGroupId(groupId: Long): List<SpendingEntity>

    @Query("SELECT * FROM spending where groupId = :groupId")
    fun getByGroupIdLive(groupId: Long): LiveData<List<SpendingEntity>>
}
