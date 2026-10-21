package app.plugbrain.android.ui.blockinterval

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.sheetdrawer.PlugBottomSheetDrawer
import app.plugbrain.android.ui.designsystem.components.slider.PlugSnapSlider
import kotlin.math.abs

/** Block intervals the slider snaps to, in minutes: the Fibonacci numbers up to 21. */
val BlockIntervalSnapValues = listOf(1, 2, 3, 5, 8, 13, 21)

/** The snap value closest to [minutes], so intervals saved before the slider (10, 15…) still open on a stop. */
fun nearestBlockInterval(minutes: Int): Int = BlockIntervalSnapValues.minBy { abs(it - minutes) }

/**
 * Lets the user pick how many minutes of use come before a challenge, on a slider that snaps to
 * [BlockIntervalSnapValues]. Nothing is saved until [onSave].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockIntervalSheet(
  blockInterval: Int,
  onSave: (Int) -> Unit,
  onDismiss: () -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  var selected by rememberSaveable { mutableIntStateOf(nearestBlockInterval(blockInterval)) }
  PlugBottomSheetDrawer(
    title = {
      Text(
        text = stringResource(R.string.dashboard_block_interval),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
      )
    },
    primaryCtaText = stringResource(R.string.block_interval_sheet_save),
    primaryCtaAction = { onSave(selected) },
    secondaryCtaText = null,
    onDismiss = onDismiss,
    sheetState = sheetState,
  ) {
    Text(
      text = stringResource(R.string.block_interval_sheet_description),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text(
        text = pluralStringResource(R.plurals.block_interval_sheet_value, selected, selected),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
      )
      val resources = LocalContext.current.resources
      PlugSnapSlider(
        value = selected,
        snapValues = BlockIntervalSnapValues,
        onValueChange = { selected = it },
        valueDescription = { resources.getQuantityString(R.plurals.block_interval_sheet_value, it, it) },
      )
    }
  }
}
