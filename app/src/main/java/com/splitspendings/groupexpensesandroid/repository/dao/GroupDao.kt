package com.splitspendings.groupexpensesandroid.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.repository.entity.Group

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: Group): Long

    @Insert
    suspend fun insertAll(groupList: List<Group>)

    @Update
    suspend fun update(group: Group): Int

    @Query("SELECT * from user_group where id = :id")
    suspend fun get(id: Long): Group?

    @Query("SELECT * from user_group where id = :id")
    fun getLive(id: Long): LiveData<Group>

    @Query("SELECT * FROM user_group")
    suspend fun getAll(): List<Group>

    @Query("SELECT * FROM user_group")
    fun getAllLive(): LiveData<List<Group>>

    @Query("DELETE FROM user_group")
    suspend fun clear()
}
