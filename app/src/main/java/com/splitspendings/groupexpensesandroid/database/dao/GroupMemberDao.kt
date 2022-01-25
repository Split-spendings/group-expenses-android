package com.splitspendings.groupexpensesandroid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.splitspendings.groupexpensesandroid.database.entity.GroupMemberEntity

@Dao
interface GroupMemberDao {

    @Query("SELECT * from group_member where id = :id")
    fun getLive(id: String): LiveData<GroupMemberEntity>

    @Query("SELECT * FROM group_member")
    fun getAllLive(): LiveData<List<GroupMemberEntity>>

    @Query("SELECT * FROM group_member where groupId = :groupId")
    fun getByGroupIdLive(groupId: Long): LiveData<List<GroupMemberEntity>>

    @Insert
    suspend fun insert(groupMember: GroupMemberEntity): Long

    @Insert
    suspend fun insertAll(groupMembersList: List<GroupMemberEntity>)

    @Update
    suspend fun update(groupMember: GroupMemberEntity): Int

    @Query("SELECT * from group_member where id = :id")
    suspend fun get(id: String): GroupMemberEntity?

    @Query("SELECT * FROM group_member")
    suspend fun getAll(): List<GroupMemberEntity>

    @Query("DELETE FROM group_member")
    suspend fun clear()

    @Query("DELETE FROM group_member where id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM group_member where groupId = :groupId")
    suspend fun deleteByGroupId(groupId: Long)

    @Query("DELETE FROM group_member where groupId = :groupId and active = :active")
    suspend fun deleteByGroupIdAndActive(groupId: Long, active: Boolean)

    @Query("SELECT * FROM group_member where groupId = :groupId")
    suspend fun getByGroupId(groupId: Long): List<GroupMemberEntity>
}
