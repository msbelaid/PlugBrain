package app.plugbrain.android.ui.challenges.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.plugbrain.android.R
import app.plugbrain.android.repository.model.MathChallenge
import app.plugbrain.android.repository.model.Operator
import app.plugbrain.android.ui.isPortrait
import kotlinx.coroutines.delay

@Composable
fun ArithChallengeScreen(
  modifier: Modifier = Modifier,
  mathChallenge: MathChallenge,
  checkAnswer: (Int) -> Unit,
) {
  var messageAnimationDone by remember { mutableStateOf(false) }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(32.dp),
    modifier = modifier.padding(top = 32.dp),
  ) {
    BlockAppsMessage(
      onAnimationDone = { messageAnimationDone = true },
    )

    if (messageAnimationDone) {
      if (isPortrait()) {
        OperationView(mathChallenge)
        ResponseInputView(checkAnswer)
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Spacer(Modifier.width(32.dp))
          OperationView(mathChallenge)
          Text("=", style = MaterialTheme.typography.displayLarge)
          ResponseInputView(checkAnswer)
        }
      }
    }
  }
}

// TODO a list of messages, pick randomly
@Composable
private fun BlockAppsMessage(onAnimationDone: () -> Unit) {
  var visible by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    visible = true
    delay(700)
    onAnimationDone()
  }

  AnimatedVisibility(
    visible = visible,
    enter = scaleIn() + expandHorizontally(
      animationSpec = tween(
        durationMillis = 800,
      ),
      expandFrom = Alignment.Start,
    ),
    exit = scaleOut(),
    label = "messageVisibility",
  ){
    Text(
      text = stringResource(R.string.blocking_message),
      style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 0.5.sp),
      textAlign = TextAlign.Center,
      modifier = Modifier.graphicsLayer {
        if (!visible) {
          alpha = 0f
          scaleX = 0.8f
        }
      },
    )
  }
}

@Composable
private fun ResponseInputView(checkAnswer: (Int) -> Unit) {
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current
  var responseText by remember { mutableStateOf("") }
  var startAnimation by remember { mutableStateOf(false) }

  val animatedAlpha by animateFloatAsState(
    targetValue = if (startAnimation) 1f else 0f,
    animationSpec = tween(durationMillis = 800, delayMillis = 200),
    label = "responseInputAlpha"
  )

  LaunchedEffect(Unit) {
    startAnimation = true
    focusRequester.requestFocus()
    keyboardController?.show()
  }

  TextField(
    value = responseText,
    onValueChange = { responseText = it },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    textStyle = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
    singleLine = true,
    trailingIcon = {
      Button(
        onClick = { if (responseText.isNotEmpty()) checkAnswer(responseText.toString().toInt()) },
      ) { Text(">", style = MaterialTheme.typography.displayLarge) }
    },
    keyboardActions = KeyboardActions(
      onDone = { checkAnswer(responseText.toString().toInt()) },
    ),
    modifier = Modifier
      .fillMaxWidth()
      .focusRequester(focusRequester)
      .focusable()
      .alpha(animatedAlpha),
  )
}

@Composable
private fun OperationView(mathChallenge: MathChallenge) {
  var startAnimation by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    startAnimation = true
  }

  AnimatedContent(
    targetState = startAnimation,
    transitionSpec = {
      slideInVertically { height -> height } + fadeIn() togetherWith
        slideOutVertically { height -> -height } + fadeOut()
    },
    label = "textScale",
  ) { visible ->
    if (visible) {
      Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier) {
        Text(
          text = mathChallenge.num1.toString(),
          style = MaterialTheme.typography.displayLarge,
        )
        Text(text = mathChallenge.operator.symbol, style = MaterialTheme.typography.displayLarge)
        Text(text = mathChallenge.num2.toString(), style = MaterialTheme.typography.displayLarge)
      }
    } else {
      Box(modifier = Modifier.size(0.dp))
    }
  }
}

@Preview(name = "Portrait", widthDp = 360, heightDp = 640)
@Composable
private fun ArithChallengePortraitScreenPreview() {
  ArithChallengeScreen(
    mathChallenge = MathChallenge(
      num1 = 999,
      num2 = 999,
      operator = Operator.ADDITION,
    ),
    checkAnswer = {},
  )
}

@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Preview(
  name = "Landscape Dark", widthDp = 640, heightDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ArithChallengeLandscapeScreenPreview() {
  ArithChallengeScreen(
    mathChallenge = MathChallenge(
      num1 = 999,
      num2 = 999,
      operator = Operator.ADDITION,
    ),
    checkAnswer = {},
  )
}
