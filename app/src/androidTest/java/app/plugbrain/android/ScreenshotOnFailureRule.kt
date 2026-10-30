package app.plugbrain.android

import android.graphics.Bitmap
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.services.storage.TestStorage
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Saves a screenshot of the whole screen when a test fails. The file goes to the test storage
 * service, so the Gradle run copies it to build/outputs/connected_android_test_additional_output
 * and CI uploads it with the test results.
 *
 * Give it a higher order than the compose rule, so it runs inside it and captures the screen
 * before the activity is closed.
 */
class ScreenshotOnFailureRule : TestWatcher() {
    override fun failed(
        e: Throwable?,
        description: Description,
    ) {
        val name = "${description.testClass.simpleName}-${description.methodName}.png"
        try {
            val screenshot = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
            if (screenshot == null) {
                Log.w(TAG, "No screenshot for $name")
                return
            }
            TestStorage().openOutputFile(name).use { screenshot.compress(Bitmap.CompressFormat.PNG, 100, it) }
            Log.i(TAG, "Saved failure screenshot $name")
        } catch (t: Throwable) {
            // A failed screenshot must not hide the test's own failure.
            Log.w(TAG, "Could not save failure screenshot $name", t)
        }
    }

    private companion object {
        const val TAG = "ScreenshotOnFailure"
    }
}
