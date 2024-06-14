package it.unito.progmob

import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class OthersDispatcherRule(
    val ioDispatcher: TestDispatcher = StandardTestDispatcher(),
    val defaultDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns ioDispatcher
        every { Dispatchers.Default } returns defaultDispatcher
    }

    override fun finished(description: Description?) {
        super.finished(description)
        clearStaticMockk(Dispatchers::class)
    }
}