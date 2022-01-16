package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.ShareEntity

@Dao
interface ShareDao {

    @Query("SELECT * from spending_share where id = :id")
    fun getLive(id: Long): LiveData<ShareEntity>

    @Query("SELECT * FROM spending_share")
    fun getAllLive(): LiveData<List<ShareEntity>>

    @Query("SELECT * FROM spending_share where spendingId = :spendingId")
    fun getBySpendingIdLive(spendingId: Long): LiveData<List<ShareEntity>>

    @Insert
    suspend fun insert(share: ShareEntity): Long

    @Insert
    suspend fun insertAll(sharesList: List<ShareEntity>)

    @Update
    suspend fun update(share: ShareEntity): Int

    @Query("SELECT * from spending_share where id = :id")
    suspend fun get(id: Long): ShareEntity?

    @Query("SELECT * FROM spending_share")
    suspend fun getAll(): List<ShareEntity>

    @Query("DELETE FROM spending_share")
    suspend fun clear()

    @Query("DELETE FROM spending_share where spendingId = :spendingId")
    suspend fun deleteBySpendingId(spendingId: Long)

    @Query("SELECT * FROM spending_share where spendingId = :spendingId")
    suspend fun getBySpendingId(spendingId: Long): List<ShareEntity>
}
