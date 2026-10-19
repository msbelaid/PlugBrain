package app.plugbrain.android.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.appsusage.model.InstalledApp
import app.plugbrain.android.ui.designsystem.components.button.PlugButtonTertiary
import app.plugbrain.android.ui.designsystem.components.layout.PlugStepScaffold
import app.plugbrain.android.ui.designsystem.components.listitem.PlugAppCheckboxRow
import app.plugbrain.android.ui.designsystem.components.textfield.PlugSearchField
import app.plugbrain.android.ui.onboarding.presentation.OnboardingStep
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState
import app.plugbrain.android.ui.theme.MathlockAppTheme
import coil3.compose.rememberAsyncImagePainter

@Composable
fun AppsStep(
  installedAppsState: InstalledAppsState,
  blockedApps: Set<String>,
  searchQuery: String,
  onQueryChange: (String) -> Unit,
  onAppCheckedChange: (packageName: String, isChecked: Boolean) -> Unit,
  onBlockApps: () -> Unit,
  onSkip: () -> Unit,
) {
  PlugStepScaffold(
    title = stringResource(R.string.onboarding_apps_title),
    subtitle = stringResource(R.string.onboarding_apps_subtitle),
    currentStep = OnboardingStep.Apps.progress,
    totalSteps = OnboardingStep.progressSteps,
    primaryCtaText = stringResource(R.string.onboarding_apps_cta),
    onPrimaryCtaClick = onBlockApps,
    isPrimaryCtaEnabled = blockedApps.isNotEmpty(),
    topAction = { PlugButtonTertiary(text = stringResource(R.string.onboarding_skip), onClick = onSkip) },
    // The app list scrolls on its own, so the title and search field stay in place.
    isScrollable = false,
  ) {
    PlugSearchField(
      query = searchQuery,
      onQueryChange = onQueryChange,
      placeholder = stringResource(R.string.onboarding_apps_search),
      clearContentDescription = stringResource(R.string.onboarding_apps_clear_search),
      modifier = Modifier.fillMaxWidth(),
    )
    when (installedAppsState) {
      is InstalledAppsState.Loading -> Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .weight(1f)
          .fillMaxSize(),
      ) {
        CircularProgressIndicator()
      }
      is InstalledAppsState.Success -> LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
      ) {
        items(installedAppsState.apps, key = { it.packageName }) { app ->
          PlugAppCheckboxRow(
            name = app.name,
            icon = rememberAsyncImagePainter(model = app.icon),
            isChecked = app.packageName in blockedApps,
            onCheckedChange = { isChecked -> onAppCheckedChange(app.packageName, isChecked) },
            // The step's content is already inset, so the rows line up with the search field.
            contentPadding = PaddingValues(vertical = 8.dp),
          )
        }
      }
    }
  }
}

@Preview(showSystemUi = true)
@Composable
private fun AppsStepPreview() {
  MathlockAppTheme(dynamicColor = false) {
    AppsStep(
      installedAppsState = InstalledAppsState.Success(
        apps = listOf(
          InstalledApp(name = "Instagram", packageName = "com.instagram.android"),
          InstalledApp(name = "Youtube", packageName = "com.google.android.youtube"),
        ),
      ),
      blockedApps = setOf("com.instagram.android"),
      searchQuery = "",
      onQueryChange = {},
      onAppCheckedChange = { _, _ -> },
      onBlockApps = {},
      onSkip = {},
    )
  }
}
