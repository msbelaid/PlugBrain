package app.plugbrain.android.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.plugbrain.android.R
import app.plugbrain.android.ui.designsystem.components.listitem.PlugNavigationListItem
import app.plugbrain.android.ui.designsystem.components.sheetdrawer.PlugBottomSheetDrawer

/** Pages outside the app that the About sheet links to. */
enum class AboutLink(val url: String) {
  SourceCode("https://github.com/msbelaid/PlugBrain"),
  ReportBug("https://github.com/msbelaid/PlugBrain/issues"),
  Website("https://plugbrain.app"),
}

/**
 * The app's version and links to its source code, issues and website, for people who want to
 * check what an app with accessibility access does. Not to releases: Play doesn't allow pointing
 * users to updates from outside the store.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutSheet(
  versionName: String,
  onLinkClick: (AboutLink) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  PlugBottomSheetDrawer(
    title = {
      Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
      )
    },
    primaryCtaText = null,
    secondaryCtaText = null,
    onDismiss = onDismiss,
    sheetState = sheetState,
    modifier = modifier,
  ) {
    Text(
      text = stringResource(R.string.about_version, versionName),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth(),
    ) {
      PlugNavigationListItem(
        title = stringResource(R.string.about_source_code_title),
        description = stringResource(R.string.about_source_code_description),
        icon = Icons.Rounded.Code,
        onClick = { onLinkClick(AboutLink.SourceCode) },
      )
      PlugNavigationListItem(
        title = stringResource(R.string.about_report_bug_title),
        description = stringResource(R.string.about_report_bug_description),
        icon = Icons.Rounded.BugReport,
        onClick = { onLinkClick(AboutLink.ReportBug) },
      )
      PlugNavigationListItem(
        title = stringResource(R.string.about_website_title),
        description = stringResource(R.string.about_website_description),
        icon = Icons.Rounded.Language,
        onClick = { onLinkClick(AboutLink.Website) },
      )
    }
  }
}
