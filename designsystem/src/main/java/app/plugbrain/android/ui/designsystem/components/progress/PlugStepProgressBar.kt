package app.plugbrain.android.ui.designsystem.components.progress

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.ui.theme.PlugBrainTheme
import app.plugbrain.android.ui.theme.PrimaryGradient

private val BarShape = RoundedCornerShape(50)

/** How far the user is in a multi-step flow; the fill animates when [currentStep] changes. */
@Composable
fun PlugStepProgressBar(
  currentStep: Int,
  totalSteps: Int,
  modifier: Modifier = Modifier,
) {
  val step = currentStep.coerceIn(0, totalSteps)
  val fraction by animateFloatAsState(
    targetValue = if (totalSteps > 0) step.toFloat() / totalSteps else 0f,
    label = "anim_step_progress",
  )
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(8.dp)
      .semantics {
        progressBarRangeInfo = ProgressBarRangeInfo(
          current = step.toFloat(),
          range = 0f..totalSteps.toFloat(),
          steps = (totalSteps - 1).coerceAtLeast(0),
        )
      }
      .clip(BarShape)
      .background(MaterialTheme.colorScheme.surfaceVariant),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(fraction)
        .fillMaxHeight()
        .clip(BarShape)
        .background(PrimaryGradient),
    )
  }
}

@Preview(
  name = "Light Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugStepProgressBarPreview() {
  PlugBrainTheme(dynamicColor = false) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp),
    ) {
      PlugStepProgressBar(currentStep = 1, totalSteps = 3)
      PlugStepProgressBar(currentStep = 2, totalSteps = 3)
      PlugStepProgressBar(currentStep = 3, totalSteps = 3)
    }
  }
}
