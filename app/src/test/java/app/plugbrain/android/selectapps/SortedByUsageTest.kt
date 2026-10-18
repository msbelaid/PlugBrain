package app.plugbrain.android.selectapps

import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.ui.selectapps.presentation.sortedByUsage
import org.junit.Assert.assertEquals
import org.junit.Test

class SortedByUsageTest {
    private val apps =
        listOf(
            InstalledApp(name = "Calculator", packageName = "com.calculator"),
            InstalledApp(name = "youtube", packageName = "com.youtube"),
            InstalledApp(name = "Instagram", packageName = "com.instagram"),
            InstalledApp(name = "Books", packageName = "com.books"),
        )

    @Test
    fun `most used apps come first`() {
        val usage = mapOf("com.youtube" to 5_000L, "com.instagram" to 9_000L)

        val sorted = apps.sortedByUsage(usage)

        assertEquals(
            listOf("Instagram", "youtube", "Books", "Calculator"),
            sorted.map { it.name },
        )
        assertEquals(listOf(9_000L, 5_000L, 0L, 0L), sorted.map { it.usageMillis })
    }

    @Test
    fun `apps with the same usage are sorted by name ignoring case`() {
        val usage = mapOf("com.youtube" to 1_000L, "com.calculator" to 1_000L)

        val sorted = apps.sortedByUsage(usage)

        assertEquals(
            listOf("Calculator", "youtube", "Books", "Instagram"),
            sorted.map { it.name },
        )
    }

    @Test
    fun `without usage data the list is alphabetical`() {
        val sorted = apps.sortedByUsage(emptyMap())

        assertEquals(
            listOf("Books", "Calculator", "Instagram", "youtube"),
            sorted.map { it.name },
        )
    }
}
