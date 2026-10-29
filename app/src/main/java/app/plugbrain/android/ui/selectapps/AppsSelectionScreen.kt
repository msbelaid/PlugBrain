package app.plugbrain.android.ui.selectapps

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState

@Composable
fun AppsSelectionScreen(
  searchQuery: String,
  onQueryChange: (String) -> Unit,
  installedAppsState: InstalledAppsState,
  blockedApps: Set<String>,
  onItemCheckedChange: (String, Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    TextField(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .testTag("SearchTextField"),
      value = searchQuery,
      onValueChange = onQueryChange,
      label = { Text("Search") },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = "Search Icon")
      },
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search,
      ),
    )

    AnimatedContent(
      targetState = installedAppsState,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      transitionSpec = {
        fadeIn(animationSpec = tween(300)) togetherWith
          fadeOut(animationSpec = tween(300))
      },
      label = "AnimatedContent", // provide a label for debugging animations
    ) { state ->
      when (state) {
        is InstalledAppsState.Loading -> {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
          ) {
            CircularProgressIndicator(modifier = Modifier.testTag("LoadingIndicator"))
          }
        }
        is InstalledAppsState.Success -> {
          InstalledAppsList(
            modifier = Modifier.fillMaxSize().testTag("AppsList"),
            installedApps = state.apps,
            maxUsageMillis = state.maxUsageMillis,
            blockedApps = blockedApps,
            onItemCheckedChange = onItemCheckedChange,
          )
        }
      }
    }
  }
}

@Composable
fun InstalledAppsList(
  installedApps: List<InstalledApp>,
  maxUsageMillis: Long,
  blockedApps: Set<String>,
  onItemCheckedChange: (String, Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(vertical = 8.dp),
  ) {
    items(
      installedApps,
      key = { it.packageName },
    ) { item ->
      InstalledAppItem(
        installedApp = item,
        usageFraction = if (maxUsageMillis > 0) item.usageMillis.toFloat() / maxUsageMillis else 0f,
        isSelected = blockedApps.contains(item.packageName),
        onCheckedChange = { checked -> onItemCheckedChange(item.packageName, checked) },
      )
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
)
@Composable
private fun AppsSelectionScreenPreview() {
  AppsSelectionScreen(
    installedAppsState = InstalledAppsState.Success(
      listOf(
        InstalledApp(
          name = "Youtube",
          packageName = "com.google.youtube",
          icon = AppCompatResources.getDrawable(
            LocalContext.current,
            R.mipmap.ic_launcher,
          )!!,
        ),
        InstalledApp(
          name = "PlugBrain",
          packageName = "app.plugbrain.android",
          icon = AppCompatResources.getDrawable(
            LocalContext.current,
            R.mipmap.ic_launcher,
          )!!,
        ),
      ),
    ),
    searchQuery = "",
    onQueryChange = {},
    onItemCheckedChange = { packageName, checked -> },
    blockedApps = emptySet(),
  )
}
