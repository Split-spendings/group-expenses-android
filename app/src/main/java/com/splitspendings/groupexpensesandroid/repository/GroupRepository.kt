package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.common.InviteOption
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.model.Balance
import com.splitspendings.groupexpensesandroid.model.Group
import com.splitspendings.groupexpensesandroid.model.GroupMember
import com.splitspendings.groupexpensesandroid.model.Spending
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.NewGroupDto
import com.splitspendings.groupexpensesandroid.network.dto.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupRepository(private val database: GroupExpensesDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: GroupRepository? = null

        fun getInstance(): GroupRepository {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = GroupRepository(GroupExpensesDatabase.getInstance(context))
                }
            }
        }
    }

    val groups: LiveData<List<Group>> = Transformations.map(database.groupDao.getAllLive()) {
        it.asModel()
    }

    fun getGroup(id: Long): LiveData<Group> = Transformations.map(database.groupDao.getLive(id)) {
        it.asModel()
    }

    suspend fun refreshGroups(filter: GroupsFilter) {
        withContext(Dispatchers.IO) {
            val groups = GroupExpensesApi.retrofitService.getFilteredGroups(filter)
            when (filter) {
                GroupsFilter.ALL -> database.groupDao.clear()
                GroupsFilter.CURRENT -> database.groupDao.deleteByCurrent(true)
                GroupsFilter.FORMER -> database.groupDao.deleteByCurrent(false)
            }
            database.groupDao.insertAll(groups.asEntity())
        }
    }

    suspend fun saveGroup(name: String): Long {
        val newGroup = NewGroupDto(
            name = name,
            personal = true,
            simplifyDebts = true,
            inviteOption = InviteOption.ALL_ACTIVE_MEMBERS,
            defaultCurrency = Currency.USD
        )
        val groupSavedOnServer = GroupExpensesApi.retrofitService.createGroup(newGroup)
        return database.groupDao.insert(groupSavedOnServer.asEntity())
    }

    fun getGroupSpendings(groupId: Long): LiveData<List<Spending>> = Transformations.map(database.spendingDao.getByGroupIdLive(groupId)) {
        it.asModel()
    }

    suspend fun refreshGroupSpendings(groupId: Long) {
        withContext(Dispatchers.IO) {
            val groupSpendings = GroupExpensesApi.retrofitService.groupSpendings(groupId)
            database.spendingDao.deleteByGroupId(groupId)
            database.spendingDao.insertAll(groupSpendings.spendings.asEntity(groupId))
        }
    }

    suspend fun joinGroupByCode(code: String): Long {
        val group = GroupExpensesApi.retrofitService.joinGroupByInviteCode(code)
        database.groupDao.delete(group.id)
        return database.groupDao.insert(group.asEntity())
    }

    suspend fun refreshInvitationCode(groupId: Long) {
        //TODO user new endpoint for getting existing invite from server
        val invitationCode = GroupExpensesApi.retrofitService.generateGroupInviteCode(groupId)
        database.groupDao.updateInvitationCode(invitationCode.code, groupId)
    }

    suspend fun generateNewInvitationCode(groupId: Long) {
        val invitationCode = GroupExpensesApi.retrofitService.generateGroupInviteCode(groupId)
        database.groupDao.updateInvitationCode(invitationCode.code, groupId)
    }

    fun getGroupBalances(groupId: Long): LiveData<List<Balance>> = Transformations.map(database.balanceDao.getByGroupIdLive(groupId)) {
        it.asModel()
    }

    suspend fun refreshGroupBalances(groupId: Long) {
        withContext(Dispatchers.IO) {
            val groupBalances = GroupExpensesApi.retrofitService.groupBalances(groupId)
            database.balanceDao.deleteByGroupId(groupId)
            database.balanceDao.insertAll(groupBalances.balances.asEntity(groupId))
        }
    }

    fun getGroupMembers(groupId: Long): LiveData<List<GroupMember>> = Transformations.map(database.groupMemberDao.getByGroupIdLive(groupId)) {
        it.asModel()
    }

    suspend fun refreshGroupMembers(groupId: Long) {
        withContext(Dispatchers.IO) {
            val groupMembers = GroupExpensesApi.retrofitService.groupActiveMembers(groupId)
            database.groupMemberDao.deleteByGroupId(groupId)
            database.groupMemberDao.insertAll(groupMembers.members.asEntity(groupId))
        }
    }
}