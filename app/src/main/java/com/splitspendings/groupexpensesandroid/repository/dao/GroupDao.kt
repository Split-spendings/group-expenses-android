package com.splitspendings.groupexpensesandroid.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.repository.entities.Group

@Dao
interface GroupDao {

    @Insert
    fun insert(group: Group)

    @Query("SELECT * from user_group where id = :id")
    fun get(id: Long) : Group?

    @Update
    fun update(group: Group)

    @Query("SELECT * FROM user_group")
    fun getAll(): List<Group>

    @Query("SELECT * FROM user_group")
    fun getAllLive(): LiveData<List<Group>>

    @Query("DELETE FROM user_group")
    fun clear()
}
