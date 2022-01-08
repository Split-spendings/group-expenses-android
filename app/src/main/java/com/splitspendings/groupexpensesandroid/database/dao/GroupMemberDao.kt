package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.GroupMemberEntity

@Dao
interface GroupMemberDao {

    @Insert
    suspend fun insert(groupMember: GroupMemberEntity)

    @Insert
    suspend fun insertAll(groupMembersList: List<GroupMemberEntity>)

    @Update
    suspend fun update(group: GroupMemberEntity): Int

    @Query("SELECT * from group_member where id = :id")
    suspend fun get(id: String): GroupMemberEntity?

    @Query("SELECT * from group_member where id = :id")
    fun getLive(id: String): LiveData<GroupMemberEntity>

    @Query("SELECT * FROM group_member")
    suspend fun getAll(): List<GroupMemberEntity>

    @Query("SELECT * FROM group_member")
    fun getAllLive(): LiveData<List<GroupMemberEntity>>

    @Query("DELETE FROM group_member")
    suspend fun clear()

    @Query("DELETE FROM group_member where id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM group_member where groupId = :groupId")
    fun getByGroupIdLive(groupId: Long): LiveData<List<GroupMemberEntity>>

    @Query("DELETE FROM group_member where groupId = :groupId")
    suspend fun deleteByGroupId(groupId: Long)
}
