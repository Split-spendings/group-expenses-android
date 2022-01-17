package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.CurrentAppUserEntity

@Dao
interface CurrentAppUserDao {

    @Query("SELECT * from current_app_user where id = :id")
    fun getLive(id: String): LiveData<CurrentAppUserEntity>

    @Query("SELECT * FROM current_app_user")
    fun getAllLive(): LiveData<List<CurrentAppUserEntity>>

    @Insert
    suspend fun insert(currentAppUser: CurrentAppUserEntity)

    @Insert
    suspend fun insertAll(currentAppUserList: List<CurrentAppUserEntity>)

    @Update
    suspend fun update(currentAppUser: CurrentAppUserEntity): Int

    @Query("SELECT * from current_app_user where id = :id")
    suspend fun get(id: String): CurrentAppUserEntity?

    @Query("SELECT * FROM current_app_user")
    suspend fun getAll(): List<CurrentAppUserEntity>

    @Query("DELETE FROM current_app_user")
    suspend fun clear()

    @Query("DELETE FROM current_app_user where id = :id")
    suspend fun delete(id: String)
}
