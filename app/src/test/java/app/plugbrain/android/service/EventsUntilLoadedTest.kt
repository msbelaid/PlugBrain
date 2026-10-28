package app.plugbrain.android.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EventsUntilLoadedTest {
    private val events = EventsUntilLoaded()

    @Test
    fun `events before the settings are read are held back`() {
        assertFalse(events.offer("com.instagram"))
    }

    @Test
    fun `the latest held back event is handled once the settings are read`() {
        events.offer("com.launcher")
        events.offer("com.instagram")

        assertEquals("com.instagram", events.markLoaded())
    }

    @Test
    fun `nothing is handled on load when no event came first`() {
        assertNull(events.markLoaded())
    }

    @Test
    fun `events after the settings are read are handled right away`() {
        events.markLoaded()

        assertTrue(events.offer("com.instagram"))
    }

    @Test
    fun `later settings updates don't replay the event again`() {
        events.offer("com.instagram")
        events.markLoaded()

        assertNull(events.markLoaded())
    }
}
