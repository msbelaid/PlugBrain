package app.plugbrain.android.ui.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.button.PlugNumericalInput
import app.plugbrain.android.ui.theme.MathlockAppTheme
import app.plugbrain.android.ui.theme.Success200

@Composable
fun PlugCard(
  challengeText: String,
  inputValue: String,
  onInputChange: (String) -> Unit,
  onCheckAnswer: (Int?) -> Unit,
  modifier: Modifier = Modifier,
  isSuccess: Boolean = false,
  isError: Boolean = false,
) {
  OutlinedCard(
    modifier = modifier
      .width(329.dp)
      .height(238.dp)
      .shadow(
        elevation = CardElevation,
        shape = CardShape,
        clip = false,
      ),
    shape = CardShape,
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface,
    ),
    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
    ),
  ) {
    Column(
      modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Challenge text
      Text(
        text = challengeText,
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
      )

      // Answer input field
      PlugNumericalInput(
        value = inputValue,
        onValueChange = onInputChange,
        onSubmit = onCheckAnswer,
        placeholder = stringResource(R.string.plug_card_input_placeholder),
        isSuccess = isSuccess,
        isError = isError,
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = InputElevation,
            shape = InputShadowShape,
          ),
      )

      // Check answer button
      Button(
        onClick = { onCheckAnswer(inputValue.toIntOrNull()) },
        enabled = inputValue.isNotEmpty(),
        shape = CheckButtonShape,
        contentPadding = CheckButtonPadding,
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          contentColor = Success200,
          disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          disabledContentColor = Success200.copy(alpha = 0.5f),
        ),
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = ButtonElevation,
            shape = CheckButtonShape,
          ),
      ) {
        Text(
          text = stringResource(R.string.plug_card_check_answer),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }
  }
}

private val CardShape = RoundedCornerShape(16.dp)
private val CheckButtonShape = RoundedCornerShape(50)
private val InputShadowShape = RoundedCornerShape(50)
private val CheckButtonPadding = PaddingValues(
  horizontal = 24.dp,
  vertical = 15.dp,
)
private val CardElevation = 4.dp
private val InputElevation = 2.dp
private val ButtonElevation = 2.dp

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
private fun PlugCardSmallNumbersPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "12 + 34",
        inputValue = "",
        onInputChange = {},
        onCheckAnswer = {},
      )
    }
  }
}

@Preview(
  name = "Light Mode - Big Numbers",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Big Numbers",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugCardBigNumbersPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "9876 x 5432",
        inputValue = "",
        onInputChange = {},
        onCheckAnswer = {},
      )
    }
  }
}

@Preview(
  name = "Light Mode - With Input",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - With Input",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugCardWithInputPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "25 + 17",
        inputValue = "42",
        onInputChange = {},
        onCheckAnswer = {},
      )
    }
  }
}

@Preview(
  name = "Light Mode - Success",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Success",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugCardSuccessPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "25 + 17",
        inputValue = "42",
        onInputChange = {},
        onCheckAnswer = {},
        isSuccess = true,
      )
    }
  }
}

@Preview(
  name = "Light Mode - Error",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Error",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugCardErrorPreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "25 + 17",
        inputValue = "99",
        onInputChange = {},
        onCheckAnswer = {},
        isError = true,
      )
    }
  }
}

@Preview(
  name = "Light Mode - Square Challenge",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Square Challenge",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugCardSquareChallengePreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "15² = ?",
        inputValue = "",
        onInputChange = {},
        onCheckAnswer = {},
      )
    }
  }
}

@Preview(
  name = "Light Mode - Missing Challenge",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode - Missing Challenge",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PlugCardMissingChallengePreview() {
  MathlockAppTheme(dynamicColor = false) {
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
    ) {
      PlugCard(
        challengeText = "? x 12 = 144",
        inputValue = "",
        onInputChange = {},
        onCheckAnswer = {},
      )
    }
  }
}
