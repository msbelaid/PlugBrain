package app.plugbrain.android.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.card.PlugIllustrationCard
import app.plugbrain.android.ui.designsystem.components.sheetdrawer.PlugBottomSheetDrawer

/**
 * Shows where to find PlugBrain in the system settings and which switch to turn on, before
 * [onUnderstood] opens that settings screen. For accessibility it also carries the disclosure
 * Google Play requires before the user enables the service.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionExplainerSheet(
  permission: OnboardingPermission,
  onUnderstood: () -> Unit,
  onDismiss: () -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  val texts = permission.explainerTexts()
  PlugBottomSheetDrawer(
    title = {
      Text(
        text = stringResource(texts.title),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
      )
    },
    primaryCtaText = stringResource(R.string.onboarding_sheet_cta),
    primaryCtaAction = onUnderstood,
    secondaryCtaText = null,
    onDismiss = onDismiss,
    sheetState = sheetState,
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .weight(1f, fill = false)
        .verticalScroll(rememberScrollState()),
    ) {
      if (permission == OnboardingPermission.Accessibility) {
        Text(
          text = stringResource(R.string.enable_accessibility_text),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
        )
      }
      PlugIllustrationCard(caption = stringResource(texts.findApp)) {
        // A mock settings list: the illustration is decorative, the caption says what to do.
        Column(
          verticalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.clearAndSetSemantics { },
        ) {
          SkeletonRow()
          Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            AppIcon()
            Text(
              text = stringResource(R.string.app_name),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
            )
          }
          SkeletonRow()
        }
      }
      PlugIllustrationCard(caption = stringResource(texts.toggle)) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clearAndSetSemantics { },
        ) {
          Text(
            text = stringResource(texts.switchLabel),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
          )
          Switch(checked = true, onCheckedChange = null)
        }
      }
    }
  }
}

// The launcher drawables are adaptive icon layers (108dp with a 72dp safe zone),
// so they are stacked and cropped to the visible area, like a launcher would.
@Composable
private fun AppIcon() {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .size(AppIconSize)
      .clip(CircleShape),
  ) {
    Image(
      painter = painterResource(R.drawable.ic_launcher_background),
      contentDescription = null,
      modifier = Modifier.requiredSize(AppIconSize * 1.5f),
    )
    Image(
      painter = painterResource(R.drawable.ic_launcher),
      contentDescription = null,
      modifier = Modifier.requiredSize(AppIconSize * 1.5f),
    )
  }
}

private val AppIconSize = 32.dp

@Composable
private fun SkeletonRow() {
  Row(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier = Modifier
        .size(AppIconSize)
        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
    )
    Box(
      modifier = Modifier
        .width(120.dp)
        .height(12.dp)
        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50)),
    )
  }
}

private class ExplainerTexts(
  val title: Int,
  val findApp: Int,
  val switchLabel: Int,
  val toggle: Int,
)

private fun OnboardingPermission.explainerTexts() = when (this) {
  OnboardingPermission.UsageAccess -> ExplainerTexts(
    title = R.string.onboarding_usage_access_sheet_title,
    findApp = R.string.onboarding_sheet_find_app,
    switchLabel = R.string.onboarding_usage_access_sheet_switch,
    toggle = R.string.onboarding_usage_access_sheet_toggle,
  )
  OnboardingPermission.Overlay -> ExplainerTexts(
    title = R.string.onboarding_overlay_sheet_title,
    findApp = R.string.onboarding_sheet_find_app,
    switchLabel = R.string.onboarding_overlay_sheet_switch,
    toggle = R.string.onboarding_overlay_sheet_toggle,
  )
  // Battery optimization and background pop-ups open their settings directly, so they never show this sheet.
  OnboardingPermission.Accessibility,
  OnboardingPermission.BatteryOptimization,
  OnboardingPermission.BackgroundPopups,
  -> ExplainerTexts(
    title = R.string.onboarding_accessibility_sheet_title,
    findApp = R.string.onboarding_accessibility_sheet_find_app,
    switchLabel = R.string.onboarding_accessibility_sheet_switch,
    toggle = R.string.onboarding_accessibility_sheet_toggle,
  )
}
