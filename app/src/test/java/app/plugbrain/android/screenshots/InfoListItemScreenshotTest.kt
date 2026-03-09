package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.listitem.PlugInfoListItem
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class InfoListItemScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun permissionListItemGrantedTest() {
        paparazzi.snapshot {
            infoItemTest()
        }
    }

    @Test
    fun permissionListItemDarkModeTest() {
        paparazzi.snapshot {
            infoItemTest(darkTheme = true)
        }
    }

    @Composable
    private fun infoItemTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(32.dp),
            ) {
                PlugInfoListItem(
                    title = "Focus your attention",
                    description = "Spend less time on apps that distract you.",
                    icon = Icons.Rounded.TrackChanges,
                )
                PlugInfoListItem(
                    title = "Unlock with a challenge",
                    description = "Solve a quick math challenge to keep using the app.",
                    icon = Icons.Rounded.LockOpen,
                )
                PlugInfoListItem(
                    title = "Gets harder the longer you stay",
                    description = "Challenges increase in difficulty the longer you keep scrolling.",
                    icon = Icons.AutoMirrored.Rounded.TrendingUp,
                )
            }
        }
    }
}
