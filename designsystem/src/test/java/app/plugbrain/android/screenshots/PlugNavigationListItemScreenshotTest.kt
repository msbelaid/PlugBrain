package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.listitem.PlugAppIconStack
import app.plugbrain.android.ui.designsystem.components.listitem.PlugNavigationListItem
import app.plugbrain.android.ui.theme.PlugBrainTheme
import org.junit.Rule
import org.junit.Test

class PlugNavigationListItemScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugNavigationListItemTest() {
        paparazzi.snapshotWithLargeFont {
            navigationListItems(darkTheme = false)
        }
    }

    @Test
    fun plugNavigationListItemDarkModeTest() {
        paparazzi.snapshot {
            navigationListItems(darkTheme = true)
        }
    }

    @Composable
    private fun navigationListItems(darkTheme: Boolean) {
        PlugBrainTheme(dynamicColor = false, darkTheme = darkTheme) {
            val appIcon = rememberVectorPainter(Icons.Rounded.Android)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
            ) {
                PlugNavigationListItem(
                    title = "7 blocked apps",
                    icon = Icons.Rounded.Apps,
                    onClick = {},
                    supportingContent = { PlugAppIconStack(icons = List(7) { appIcon }) },
                )
                PlugNavigationListItem(
                    title = "Block interval",
                    description = "Every 15 minutes of use",
                    icon = Icons.Rounded.Schedule,
                    onClick = {},
                )
            }
        }
    }
}
