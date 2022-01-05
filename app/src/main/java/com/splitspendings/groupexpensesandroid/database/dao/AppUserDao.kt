package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.AppUserEntity

@Dao
interface AppUserDao {

    @Insert
    suspend fun insert(appUser: AppUserEntity)

    @Insert
    suspend fun insertAll(appUsersList: List<AppUserEntity>)

    @Update
    suspend fun update(group: AppUserEntity): Int

    @Query("SELECT * from app_user where id = :id")
    suspend fun get(id: String): AppUserEntity?

    @Query("SELECT * from app_user where id = :id")
    fun getLive(id: String): LiveData<AppUserEntity>

    @Query("SELECT * FROM app_user")
    suspend fun getAll(): List<AppUserEntity>

    @Query("SELECT * FROM app_user")
    fun getAllLive(): LiveData<List<AppUserEntity>>

    @Query("DELETE FROM app_user")
    suspend fun clear()

    @Query("DELETE FROM app_user where id = :id")
    suspend fun delete(id: String)
}
