package app.plugbrain.android.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonPrimary
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonSecondary
import app.plugbrain.android.ui.theme.PlugBrainTheme

private val ScrimColor = Color.Black.copy(alpha = 0.6f)

/**
 * Asks, over the calculator, whether to go back to the challenge or use the calculator anyway. It
 * never blocks the calculator (#236); going back is the primary choice.
 */
@Composable
fun CalculatorDialog(
  onBackToChallenge: () -> Unit,
  onUseCalculator: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ScrimColor)
      .safeDrawingPadding()
      .padding(24.dp),
    contentAlignment = Alignment.Center,
  ) {
    Surface(
      shape = RoundedCornerShape(28.dp),
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.widthIn(max = 400.dp),
    ) {
      Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
      ) {
        Box(
          modifier = Modifier
            .size(56.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Outlined.Calculate,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp),
          )
        }
        Text(
          text = stringResource(R.string.calculator_dialog_title),
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          PlugButtonPrimary(
            text = stringResource(R.string.calculator_dialog_back_to_challenge),
            onClick = onBackToChallenge,
            modifier = Modifier.fillMaxWidth(),
          )
          PlugButtonSecondary(
            text = stringResource(R.string.calculator_dialog_use_calculator),
            onClick = onUseCalculator,
            modifier = Modifier.fillMaxWidth(),
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun CalculatorDialogPreview() {
  PlugBrainTheme(dynamicColor = false) {
    CalculatorDialog(onBackToChallenge = {}, onUseCalculator = {})
  }
}
