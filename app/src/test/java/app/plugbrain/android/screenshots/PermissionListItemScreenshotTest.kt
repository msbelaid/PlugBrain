package app.plugbrain.android.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.plugbrain.android.ui.designsystem.components.listitem.PermissionListItem
import app.plugbrain.android.ui.theme.MathlockAppTheme
import org.junit.Rule
import org.junit.Test

class PermissionListItemScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
        )

    @Test
    fun permissionListItemGrantedTest() {
        paparazzi.snapshot {
            permissionItemTest()
        }
    }

    @Test
    fun permissionListItemDarkModeTest() {
        paparazzi.snapshot {
            permissionItemTest(darkTheme = true)
        }
    }

    @Composable
    private fun permissionItemTest(darkTheme: Boolean = false) {
        MathlockAppTheme(dynamicColor = false, darkTheme = darkTheme) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(32.dp),
            ) {
                PermissionListItem(
                    title = "App Usage access",
                    description = "Lets PlugBrain track app usage so it can block distracting apps at the right time.",
                    isGranted = true,
                    icon = Icons.Filled.Search,
                    onClick = {},
                )
                PermissionListItem(
                    title = "Display over other apps",
                    description = "Allows PlugBrain to display a challenge while you use a distracting app.",
                    isGranted = false,
                    icon = Icons.Rounded.Layers,
                    onClick = {},
                )
            }
        }
    }
}
