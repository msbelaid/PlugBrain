package app.matholck.android.ui.challenges.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeTopBar(
  onRefreshClicked: () -> Unit,
) {
  TopAppBar(
    title = { Text("My App") },
    actions = {
      IconButton(onClick = {
        onRefreshClicked()
      }) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Refresh",
        )
      }
    },
  )
}
