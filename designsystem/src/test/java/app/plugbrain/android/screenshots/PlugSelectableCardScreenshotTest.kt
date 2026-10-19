package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SignalCellularAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.listitem.PlugSelectableCard
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PlugSelectableCardScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun plugSelectableCardTest() {
        paparazzi.snapshotWithLargeFont {
            selectableCardTest()
        }
    }

    @Test
    fun plugSelectableCardDarkModeTest() {
        paparazzi.snapshot {
            selectableCardTest(darkTheme = true)
        }
    }

    @Composable
    private fun selectableCardTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp)
                        .selectableGroup(),
            ) {
                PlugSelectableCard(
                    title = "Every 5 minutes",
                    icon = Icons.Rounded.Schedule,
                    isSelected = true,
                    onClick = {},
                )
                PlugSelectableCard(
                    title = "Every 15 minutes",
                    icon = Icons.Rounded.Schedule,
                    isSelected = false,
                    onClick = {},
                )
                PlugSelectableCard(
                    title = "Beginner",
                    description = "Simple additions and subtractions",
                    icon = Icons.Rounded.SignalCellularAlt,
                    isSelected = true,
                    onClick = {},
                )
                PlugSelectableCard(
                    title = "Intermediate",
                    description = "Multiplications and missing numbers",
                    icon = Icons.Rounded.SignalCellularAlt,
                    isSelected = false,
                    onClick = {},
                )
            }
        }
    }
}
