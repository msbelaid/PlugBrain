package app.plugbrain.android.ui.designsystem.components.layout

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonTertiary
import app.plugbrain.android.ui.designsystem.components.listitem.PlugPermissionListItem
import app.plugbrain.android.ui.designsystem.components.progress.PlugStepProgressBar
import app.plugbrain.android.ui.theme.MathlockAppTheme

private val HorizontalPadding = 24.dp

// Height of the fade above the call to action.
private val BottomBarFadeHeight = 32.dp

/**
 * Page layout for one step of a flow: an optional progress bar and top action, a title and
 * subtitle, the content, and a call to action pinned to the bottom.
 *
 * With [isScrollable] the title and content scroll together, so nothing gets clipped at large
 * font sizes. Pass false when the content brings its own lazy list; the content area then gets
 * the remaining height.
 */
@Composable
fun PlugStepScaffold(
  primaryCtaText: String,
  onPrimaryCtaClick: () -> Unit,
  modifier: Modifier = Modifier,
  title: String? = null,
  subtitle: String? = null,
  currentStep: Int = 0,
  totalSteps: Int = 0,
  isPrimaryCtaEnabled: Boolean = true,
  helperText: String? = null,
  topAction: (@Composable () -> Unit)? = null,
  background: Brush? = null,
  isScrollable: Boolean = true,
  content: @Composable ColumnScope.() -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .then(if (background != null) Modifier.background(background) else Modifier)
      .safeDrawingPadding(),
  ) {
    if (totalSteps > 0 || topAction != null) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = HorizontalPadding, end = HorizontalPadding, top = 16.dp),
      ) {
        if (totalSteps > 0) {
          PlugStepProgressBar(
            currentStep = currentStep,
            totalSteps = totalSteps,
            modifier = Modifier.weight(1f),
          )
        } else {
          Spacer(Modifier.weight(1f))
        }
        topAction?.invoke()
      }
    }
    // The bar overlays the bottom of the content, so content scrolling under it fades out.
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
          .fillMaxSize()
          .then(
            if (isScrollable) {
              // Inside the scroll, so the last item can scroll clear of the bar.
              Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = HorizontalPadding, end = HorizontalPadding, top = 24.dp)
                .padding(bottom = bottomBarHeight)
            } else {
              // A lazy list scrolls on its own, so only its last few dp sit under the fade.
              Modifier
                .padding(start = HorizontalPadding, end = HorizontalPadding, top = 24.dp)
                .padding(bottom = (bottomBarHeight - BottomBarFadeHeight).coerceAtLeast(0.dp))
            },
          ),
      ) {
        if (title != null || subtitle != null) {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (title != null) {
              Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() },
              )
            }
            if (subtitle != null) {
              Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        }
        content()
      }
      val surface = MaterialTheme.colorScheme.surface
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .onSizeChanged { bottomBarHeight = with(density) { it.height.toDp() } }
          .background(
            // Fully opaque from the top of the button down, so content never shows through it.
            Brush.verticalGradient(
              colors = listOf(surface.copy(alpha = 0f), surface),
              endY = with(density) { BottomBarFadeHeight.toPx() },
            ),
          )
          .padding(start = HorizontalPadding, end = HorizontalPadding, top = BottomBarFadeHeight, bottom = 16.dp),
      ) {
        PlugButtonPrimary(
          text = primaryCtaText,
          onClick = onPrimaryCtaClick,
          enabled = isPrimaryCtaEnabled,
          modifier = Modifier.fillMaxWidth(),
        )
        if (helperText != null) {
          Text(
            text = helperText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}

@Preview(
  name = "Light Mode",
  showSystemUi = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode",
  showSystemUi = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugStepScaffoldPreview() {
  MathlockAppTheme(dynamicColor = false) {
    PlugStepScaffold(
      title = "Let's get set up",
      subtitle = "PlugBrain needs the following permissions to work correctly.",
      currentStep = 1,
      totalSteps = 3,
      primaryCtaText = "Proceed",
      onPrimaryCtaClick = {},
      isPrimaryCtaEnabled = false,
      helperText = "Allow the required permissions to proceed.",
      topAction = { PlugButtonTertiary(text = "Skip", onClick = {}) },
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PlugPermissionListItem(
          title = "App Usage access",
          description = "Lets PlugBrain track app usage so it can block distracting apps at the right time.",
          icon = Icons.Filled.Search,
          isGranted = true,
          onClick = {},
        )
        PlugPermissionListItem(
          title = "Display over other apps",
          description = "Allows PlugBrain to display a challenge while you use a distracting app.",
          icon = Icons.Rounded.Layers,
          isGranted = false,
          onClick = {},
        )
      }
    }
  }
}
