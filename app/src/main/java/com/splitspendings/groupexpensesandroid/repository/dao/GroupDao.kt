package com.splitspendings.groupexpensesandroid.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.repository.entity.GroupEntity

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: GroupEntity): Long

    @Insert
    suspend fun insertAll(groupList: List<GroupEntity>)

    @Update
    suspend fun update(group: GroupEntity): Int

    @Query("SELECT * from user_group where id = :id")
    suspend fun get(id: Long): GroupEntity?

    @Query("SELECT * from user_group where id = :id")
    fun getLive(id: Long): LiveData<GroupEntity>

    @Query("SELECT * FROM user_group")
    suspend fun getAll(): List<GroupEntity>

    @Query("SELECT * FROM user_group")
    fun getAllLive(): LiveData<List<GroupEntity>>

    @Query("DELETE FROM user_group")
    suspend fun clear()
}
