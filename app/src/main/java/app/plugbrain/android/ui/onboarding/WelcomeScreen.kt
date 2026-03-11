package app.plugbrain.android.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.listitem.PlugInfoListItem
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.theme.Primary500

@Composable
fun WelcomeScreen() {
  Scaffold(
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = Modifier,
    topBar = {},
    bottomBar = {
      PlugButtonPrimary(
        text = stringResource(R.string.welcome_cta_title),
        modifier = Modifier
          .fillMaxWidth()
          .padding(32.dp),
        onClick = {},
      )
    },
  ) { paddingValues ->
    var size by remember { mutableStateOf(IntSize.Zero) }
    Column(
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .onSizeChanged { size = it }
        .fillMaxSize()
        .background(CircularGradient(size))
        .padding(paddingValues)
        .padding(32.dp),
    ) {
      Image(painterResource(R.drawable.ic_launcher), "", modifier = Modifier.size(144.dp))
      Text(stringResource(R.string.app_name), style = MaterialTheme.typography.displayLarge)
      Text(
        text = stringResource(R.string.welcome_title),
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
        textAlign = TextAlign.Center,
      )
      Spacer(Modifier.height(8.dp))
      PlugInfoListItem(
        modifier = Modifier.wrapContentSize(),
        title = stringResource(R.string.welcome_feature_one_title),
        description = stringResource(R.string.welcome_feature_one_description),
        icon = Icons.Rounded.TrackChanges,
      )
      PlugInfoListItem(
        title = stringResource(R.string.welcome_feature_two_title),
        description = stringResource(R.string.welcome_feature_two_description),
        icon = Icons.Rounded.LockOpen,
      )
      PlugInfoListItem(
        title = stringResource(R.string.welcome_feature_three_title),
        description = stringResource(R.string.welcome_feature_three_description),
        icon = Icons.AutoMirrored.Rounded.TrendingUp,
      )
    }
  }
}

@Composable
private fun CircularGradient(size: IntSize): Brush = Brush.radialGradient(
  colors = listOf(Primary500, MaterialTheme.colorScheme.surface),
  center = Offset(x = size.width / 2f, y = -size.height / 3f),
  radius = 1500f,
)

@Preview(showSystemUi = true)
@Composable
private fun WelcomeScreenPreview() {
  MathlockAppTheme(dynamicColor = false) {
    WelcomeScreen()
  }
}
