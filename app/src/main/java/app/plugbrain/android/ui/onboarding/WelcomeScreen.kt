package app.plugbrain.android.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.layout.PlugStepScaffold
import app.plugbrain.android.ui.designsystem.components.listitem.PlugInfoListItem
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.theme.Primary500

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
  PlugStepScaffold(
    primaryCtaText = stringResource(R.string.welcome_cta_title),
    onPrimaryCtaClick = onGetStarted,
    background = TopGradient(),
  ) {
    Image(
      painter = painterResource(R.drawable.ic_launcher),
      contentDescription = null,
      modifier = Modifier
        .size(144.dp)
        .align(Alignment.CenterHorizontally),
    )
    Text(
      text = stringResource(R.string.welcome_headline),
      style = MaterialTheme.typography.headlineLarge,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier
        .fillMaxWidth()
        .semantics { heading() },
    )
    Spacer(Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
      PlugInfoListItem(
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
private fun TopGradient(): Brush = Brush.verticalGradient(
  0f to Primary500.copy(alpha = 0.5f),
  0.4f to MaterialTheme.colorScheme.surface,
)

@Preview(showSystemUi = true)
@Composable
private fun WelcomeScreenPreview() {
  MathlockAppTheme(dynamicColor = false) {
    WelcomeScreen(onGetStarted = {})
  }
}
