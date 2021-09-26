package com.splitspendings.groupexpensesandroid.repository.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.splitspendings.groupexpensesandroid.repository.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.repository.entities.Group
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GroupDaoTest {

    private lateinit var groupDao: GroupDao
    private lateinit var db: GroupExpensesDatabase

    companion object {
        private val group1: Group = Group(id = 1, name = "group_1")
        private val group2: Group = Group(id = 2, name = "group_2")
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
    fun insertAndGet() {
        groupDao.insert(group1)
        assertEquals(group1, groupDao.get(group1.id!!))
        groupDao.insert(group2)
        assertEquals(group2, groupDao.get(group2.id!!))
    }

    @Test
    fun getAll() {
        assertEquals(0, groupDao.getAll().size)
        groupDao.insert(group1)
        assertEquals(1, groupDao.getAll().size)
        groupDao.insert(group2)
        assertEquals(2, groupDao.getAll().size)
    }

    @Test
    fun update() {
        groupDao.insert(group1)
        group1.name = "updated_name"
        groupDao.update(group1)
        assertEquals("updated_name", groupDao.get(group1.id!!)!!.name)
    }

    @Test
    fun clear() {
        groupDao.insert(group1)
        groupDao.insert(group2)
        assertEquals(2, groupDao.getAll().size)
        groupDao.clear()
        assertEquals(0, groupDao.getAll().size)
    }
}