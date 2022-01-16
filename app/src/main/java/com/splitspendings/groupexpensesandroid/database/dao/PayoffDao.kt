package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.PayoffEntity

@Dao
interface PayoffDao {

    @Query("SELECT * from payoff where id = :id")
    fun getLive(id: Long): LiveData<PayoffEntity>

    @Query("SELECT * FROM payoff")
    fun getAllLive(): LiveData<List<PayoffEntity>>

    @Query("SELECT * FROM payoff where groupId = :groupId")
    fun getByGroupIdLive(groupId: Long): LiveData<List<PayoffEntity>>

    @Insert
    suspend fun insert(payoff: PayoffEntity): Long

    @Insert
    suspend fun insertAll(payoffList: List<PayoffEntity>)

    @Update
    suspend fun update(payoff: PayoffEntity): Int

    @Query("SELECT * from payoff where id = :id")
    suspend fun get(id: Long): PayoffEntity?

    @Query("SELECT * FROM payoff")
    suspend fun getAll(): List<PayoffEntity>

    @Query("DELETE FROM payoff")
    suspend fun clear()

    @Query("DELETE FROM payoff where groupId = :groupId")
    suspend fun deleteByGroupId(groupId: Long)

    @Query("SELECT * FROM payoff where groupId = :groupId")
    suspend fun getByGroupId(groupId: Long): List<PayoffEntity>

    @Query("DELETE FROM payoff where id = :id")
    suspend fun deleteById(id: Long)
}
