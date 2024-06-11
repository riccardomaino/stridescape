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
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@UninstallModules(RoomModule::class, AppModule::class)
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
    fun insertAndGetTarget() = runTest {
        val target = TargetEntity("2023-11-15", 10000)
        targetDao.upsertNewTarget(target)
        val retrievedTarget = targetDao.findTargetByDate("2023-11-15").first()
        assertEquals(retrievedTarget, 10000)
    }

    @Test
    @Throws(Exception::class)
    fun getLastTarget() = runTest {
        val target1 = TargetEntity("2023-11-14", 8000)
        val target2 = TargetEntity("2023-11-15", 10000)
        targetDao.upsertNewTarget(target1)
        targetDao.upsertNewTarget(target2)
        val lastTarget = targetDao.findLastTarget().first()
        assertEquals(lastTarget, 10000)
    }

    @Test
    @Throws(Exception::class)
    fun upsertTarget() = runTest {
        val target1 = TargetEntity("2023-11-15", 10000)
        targetDao.upsertNewTarget(target1)
        val target2 = TargetEntity("2023-11-15", 12000)
        targetDao.upsertNewTarget(target2)
        val retrievedTarget = targetDao.findTargetByDate("2023-11-15").first()
        assertEquals(retrievedTarget, 12000)
    }

    @Test
    @Throws(Exception::class)
    fun getTargetBetweenDates() = runTest {
        val target1 = TargetEntity("2023-11-14", 8000)
        val target2 = TargetEntity("2023-11-15", 10000)
        val target3 = TargetEntity("2023-11-16", 12000)
        targetDao.upsertNewTarget(target1)
        targetDao.upsertNewTarget(target2)
        targetDao.upsertNewTarget(target3)
        val firstTarget = targetDao.findTargetBetweenDates("2023-11-15", "2023-11-15").first()
        val lastTarget = targetDao.findTargetBetweenDates("2023-11-14", "2023-11-16").last()
        assertEquals(firstTarget, 8000)
        assertEquals(lastTarget, 12000)
    }
}