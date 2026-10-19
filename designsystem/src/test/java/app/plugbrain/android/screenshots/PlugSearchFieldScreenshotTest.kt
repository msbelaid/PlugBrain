package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.textfield.PlugSearchField
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugSearchFieldScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugSearchFieldTest() {
        paparazzi.snapshotWithLargeFont {
            searchFieldTest()
        }
    }

    @Test
    fun plugSearchFieldDarkModeTest() {
        paparazzi.snapshot {
            searchFieldTest(darkTheme = true)
        }
    }

    @Composable
    private fun searchFieldTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp),
            ) {
                PlugSearchField(
                    query = "",
                    onQueryChange = {},
                    placeholder = "Search",
                    clearContentDescription = "Clear search",
                )
                PlugSearchField(
                    query = "Insta",
                    onQueryChange = {},
                    placeholder = "Search",
                    clearContentDescription = "Clear search",
                )
            }
        }
    }
}
