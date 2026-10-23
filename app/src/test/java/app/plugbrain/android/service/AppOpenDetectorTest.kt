package app.plugbrain.android.service

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppOpenDetectorTest {
    private val distractiveApps = setOf("com.instagram", "com.youtube")
    private val detector = AppOpenDetector { setOf("app.plugbrain.android", "com.android.systemui") }

    @Test
    fun `opening a distractive app from the launcher is an open`() {
        detector.isAppOpening("com.launcher", distractiveApps)

        assertTrue(detector.isAppOpening("com.instagram", distractiveApps))
    }

    @Test
    fun `the first event for a distractive app is an open`() {
        assertTrue(detector.isAppOpening("com.instagram", distractiveApps))
    }

    @Test
    fun `moving between screens of the same app is not an open`() {
        detector.isAppOpening("com.instagram", distractiveApps)

        assertFalse(detector.isAppOpening("com.instagram", distractiveApps))
    }

    @Test
    fun `returning to the app after solving the challenge is not an open`() {
        detector.isAppOpening("com.instagram", distractiveApps)
        detector.isAppOpening("app.plugbrain.android", distractiveApps)

        assertFalse(detector.isAppOpening("com.instagram", distractiveApps))
    }

    @Test
    fun `closing the notification shade is not an open`() {
        detector.isAppOpening("com.instagram", distractiveApps)
        detector.isAppOpening("com.android.systemui", distractiveApps)

        assertFalse(detector.isAppOpening("com.instagram", distractiveApps))
    }

    @Test
    fun `switching from one distractive app to another is an open`() {
        detector.isAppOpening("com.instagram", distractiveApps)

        assertTrue(detector.isAppOpening("com.youtube", distractiveApps))
    }

    @Test
    fun `reopening an app after leaving it is an open`() {
        detector.isAppOpening("com.instagram", distractiveApps)
        detector.isAppOpening("com.launcher", distractiveApps)

        assertTrue(detector.isAppOpening("com.instagram", distractiveApps))
    }

    @Test
    fun `opening an app that is not distractive is not an open`() {
        assertFalse(detector.isAppOpening("com.calendar", distractiveApps))
    }

    @Test
    fun `an open is challenged when no challenge was ever solved`() {
        assertTrue(isOpenChallengeDue(lastUnblock = null, blockIntervalMinutes = 5, now = 1_000L))
    }

    @Test
    fun `an open is not challenged within the block interval of a solve`() {
        val solvedAt = 1_000_000L

        assertFalse(isOpenChallengeDue(solvedAt, blockIntervalMinutes = 5, now = solvedAt + 4 * 60_000L))
    }

    @Test
    fun `an open is challenged once the block interval has passed since a solve`() {
        val solvedAt = 1_000_000L

        assertTrue(isOpenChallengeDue(solvedAt, blockIntervalMinutes = 5, now = solvedAt + 5 * 60_000L))
    }
}
