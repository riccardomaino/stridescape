package it.unito.progmob.core.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.di.AppModule
import it.unito.progmob.di.RoomModule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@UninstallModules(RoomModule::class , AppModule::class)
class TargetDaoTest {
    @get:Rule val hiltRule = HiltAndroidRule(this)

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var walkDatabase: WalkDatabase
    private lateinit var targetDao: TargetDao

    @Before
    fun setup() {
        hiltRule.inject()
        targetDao = walkDatabase.targetDao
    }

    @After
    fun tearDown() {
        walkDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTarget() = runBlocking {
        val target = TargetEntity("2023-11-15", 10000)
        targetDao.upsertNewTarget(target)
        val retrievedTarget = targetDao.findTargetByDate("2023-11-15").first()
        assertEquals(retrievedTarget, 10000)
    }

    @Test
    @Throws(Exception::class)
    fun getLastTarget() = runBlocking {
        val target1 = TargetEntity("2023-11-14", 8000)
        val target2 = TargetEntity("2023-11-15", 10000)
        targetDao.upsertNewTarget(target1)
        targetDao.upsertNewTarget(target2)
        val lastTarget = targetDao.findLastTarget().first()
        assertEquals(lastTarget, 10000)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTarget() = runBlocking {
        val target1 = TargetEntity("2023-11-15", 10000)
        targetDao.upsertNewTarget(target1)
        val target2 = TargetEntity("2023-11-15", 12000)
        targetDao.upsertNewTarget(target2)
        val retrievedTarget = targetDao.findTargetByDate("2023-11-15").first()
        assertEquals(retrievedTarget, 12000)
    }
}