package app.plugbrain.android.ui.challenges.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.sheetdrawer.PlugBottomSheetDrawer
import app.plugbrain.android.ui.theme.PlugBrainTheme

/**
 * Asked after a correct answer: staying focused is the primary action, unlocking the secondary one.
 * Dismissing the sheet (swipe down, tap outside, back) counts as staying focused.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockSheet(
  unlockMinutes: Int,
  onUnlockApp: () -> Unit,
  onStayFocusedClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  PlugBottomSheetDrawer(
    modifier = modifier,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(
          imageVector = Icons.Rounded.Check,
          tint = MaterialTheme.colorScheme.tertiary,
          contentDescription = null,
          modifier = Modifier.size(32.dp),
        )
        Text(
          text = stringResource(R.string.challenge_correct_answer),
          style = MaterialTheme.typography.headlineMedium,
          textAlign = TextAlign.Center,
        )
      }
    },
    primaryCtaText = stringResource(R.string.challenge_stay_focused),
    primaryCtaAction = onStayFocusedClick,
    primaryCtaIcon = Icons.Rounded.TrackChanges,
    secondaryCtaText = stringResource(R.string.challenge_unlock_app, unlockMinutes),
    secondaryCtaAction = onUnlockApp,
    secondaryCtaIcon = Icons.Rounded.LockOpen,
    onDismiss = onStayFocusedClick,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
  ) {
    Text(
      text = pluralStringResource(R.plurals.challenge_unlock_message, unlockMinutes, unlockMinutes),
      style = MaterialTheme.typography.bodyLarge,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .align(Alignment.CenterHorizontally),
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UnlockSheetPreview() {
  PlugBrainTheme(dynamicColor = false) {
    UnlockSheet(unlockMinutes = 5, onUnlockApp = {}, onStayFocusedClick = {})
  }
}
