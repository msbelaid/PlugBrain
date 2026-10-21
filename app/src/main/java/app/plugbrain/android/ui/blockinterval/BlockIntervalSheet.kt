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

/** Block intervals the slider snaps to and labels, in minutes; any minute between 1 and 15 can be picked. */
val BlockIntervalSnapValues = listOf(1, 2, 5, 10, 15)

/** [minutes] kept within the slider's range, so an interval saved outside it still opens on the slider. */
fun sliderBlockInterval(minutes: Int): Int = minutes.coerceIn(BlockIntervalSnapValues.first(), BlockIntervalSnapValues.last())

/**
 * Lets the user pick how many minutes of use come before a challenge, on a slider that snaps to
 * [BlockIntervalSnapValues] and stops on every minute in between. Nothing is saved until [onSave].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockIntervalSheet(
  blockInterval: Int,
  onSave: (Int) -> Unit,
  onDismiss: () -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  var selected by rememberSaveable { mutableIntStateOf(sliderBlockInterval(blockInterval)) }
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
