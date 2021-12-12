package com.splitspendings.groupexpensesandroid.repository.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.splitspendings.groupexpensesandroid.repository.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.repository.entity.Group
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GroupDaoTest {

    private lateinit var groupDao: GroupDao
    private lateinit var db: GroupExpensesDatabase

    companion object {
        private val group1: Group = Group(id = -1, name = "group_1", false)
        private val group2: Group = Group(id = -2, name = "group_2", true)
    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the process is killed.
        db = Room.inMemoryDatabaseBuilder(context, GroupExpensesDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        groupDao = db.groupDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGet() = runBlocking {
        val group1Id = groupDao.insert(group1)
        assertEquals(group1.id, group1Id)
        assertEquals(group1, groupDao.get(group1.id))
        val group2Id = groupDao.insert(group2)
        assertEquals(group2.id, group2Id)
        assertEquals(group2, groupDao.get(group2.id))
    }

    @Test
    fun getAll() = runBlocking {
        assertEquals(0, groupDao.getAll().size)
        groupDao.insert(group1)
        assertEquals(1, groupDao.getAll().size)
        groupDao.insert(group2)
        assertEquals(2, groupDao.getAll().size)
    }

    @Test
    fun update() = runBlocking {
        groupDao.insert(group1)
        groupDao.insert(group2)
        group1.name = "updated_name"
        group2.name = "updated_name_2"
        val updatedCountGroup1 = groupDao.update(group1)
        val updatedCountGroup2 = groupDao.update(group2)
        assertEquals(1, updatedCountGroup1)
        assertEquals(1, updatedCountGroup2)
        assertEquals("updated_name", groupDao.get(group1.id)!!.name)
        assertEquals("updated_name_2", groupDao.get(group2.id)!!.name)
    }

    @Test
    fun clear() = runBlocking {
        groupDao.insert(group1)
        groupDao.insert(group2)
        assertEquals(2, groupDao.getAll().size)
        groupDao.clear()
        assertEquals(0, groupDao.getAll().size)
    }
}