package app.plugbrain.android.appsusage

import android.app.usage.UsageEvents.Event
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Which Android usage events open and close a session in [UsageCalculator]. */
class AppEventTypeTest {
    @Test
    fun `a resumed activity starts a session`() {
        assertEquals(EventType.APP_STARTED, appEventType(Event.ACTIVITY_RESUMED))
    }

    @Test
    fun `a paused or stopped activity stops a session`() {
        assertEquals(EventType.APP_STOPPED, appEventType(Event.ACTIVITY_PAUSED))
        assertEquals(EventType.APP_STOPPED, appEventType(Event.ACTIVITY_STOPPED))
    }

    @Test
    fun `screen off ends every session`() {
        assertEquals(EventType.SCREEN_OFF, appEventType(Event.SCREEN_NON_INTERACTIVE))
    }

    @Test
    fun `shutdown ends every session like screen off`() {
        assertEquals(EventType.SCREEN_OFF, appEventType(Event.DEVICE_SHUTDOWN))
    }

    @Test
    fun `other events are ignored`() {
        assertNull(appEventType(Event.SCREEN_INTERACTIVE))
        assertNull(appEventType(Event.DEVICE_STARTUP))
        assertNull(appEventType(Event.FOREGROUND_SERVICE_START))
        assertNull(appEventType(Event.KEYGUARD_SHOWN))
        assertNull(appEventType(Event.CONFIGURATION_CHANGE))
    }
}
