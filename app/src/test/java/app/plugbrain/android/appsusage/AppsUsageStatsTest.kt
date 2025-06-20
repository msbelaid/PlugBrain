package app.plugbrain.android.appsusage

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import app.plugbrain.android.appsusage.AppsUsageStats
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class AppsUsageStatsTest {
    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var usageStatsManager: UsageStatsManager

    @Mock
    private lateinit var usageEvents: UsageEvents

    private lateinit var appsUsageStats: AppsUsageStats

    private val startTime = 1000L
    private val endTime = 5000L
    private val packageSet = setOf("com.example.app1", "com.example.app2")
    private val mockEvent = mock(UsageEvents.Event::class.java)

    @Before
    fun setup() {
        whenever(context.getSystemService(Context.USAGE_STATS_SERVICE)).thenReturn(usageStatsManager)
        whenever(usageStatsManager.queryEvents(startTime, endTime)).thenReturn(usageEvents)

        appsUsageStats = AppsUsageStats(context)
    }

    @Test
    fun `getTotalAppsUsageDuration should return zero when no events`() {
        whenever(usageEvents.hasNextEvent()).thenReturn(false)
        val result = appsUsageStats.getTotalAppsUsageDuration(startTime, endTime, packageSet)
        assertEquals(0L, result)
    }
}
