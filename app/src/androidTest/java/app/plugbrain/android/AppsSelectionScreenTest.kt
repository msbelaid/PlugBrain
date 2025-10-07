package app.plugbrain.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.plugbrain.android.repository.model.InstalledApp
import app.plugbrain.android.ui.selectapps.AppsSelectionScreen
import app.plugbrain.android.ui.selectapps.presentation.InstalledAppsState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppsSelectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Dummy data for our tests
    private val sampleApps = listOf(
        InstalledApp(name = "App One", packageName = "com.app.one"),
        InstalledApp(name = "App Two", packageName = "com.app.two")
    )


    @Test
    fun whenStateIsLoading_showsLoadingIndicator() {
        // ARRANGE: Set the screen to the Loading state
        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Loading,
                blockedApps = emptySet(),
                onItemClicked = { _, _ -> }
            )
        }

        composeTestRule.waitForIdle()

        // ASSERT: The loading indicator is displayed
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
        // ASSERT: The app list is not displayed
        composeTestRule.onNodeWithTag("AppsList").assertDoesNotExist()
    }

    @Test
    fun whenStateIsSuccessWithApps_showsAppList() {
        // ARRANGE: Set the screen to the Success state with a list of apps
        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Success(sampleApps),
                blockedApps = emptySet(),
                onItemClicked = { _, _ -> }
            )
        }

        composeTestRule.waitForIdle()

        // ASSERT: The list is displayed and an item is visible
        composeTestRule.onNodeWithTag("AppsList").assertIsDisplayed()
        composeTestRule.onNodeWithText("App One").assertIsDisplayed()
        // ASSERT: The loading indicator is not displayed
        composeTestRule.onNodeWithTag("LoadingIndicator").assertDoesNotExist()
    }

    @Test
    fun whenItemIsClicked_onItemClickedCallbackIsTriggered() {
        // ARRANGE: Set up variables to capture the callback output
        var clickedPackage: String? = null
        var isChecked: Boolean? = null

        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Success(sampleApps),
                blockedApps = emptySet(),
                onItemClicked = { pkg, checked ->
                    clickedPackage = pkg
                    isChecked = checked
                }
            )
        }
        composeTestRule.waitForIdle()

        // ACT: Find the first item and click it
        composeTestRule.onNodeWithText("App One").performClick()

        composeTestRule.waitForIdle()
        // ASSERT: Check that the callback was triggered with the correct data
        assertEquals("com.app.one", clickedPackage)
        assertEquals(true, isChecked)
    }

    @Test
    fun whenSearchQueryChanges_onQueryChangedCallbackIsTriggered() {
        // ARRANGE: Set up a variable to capture the callback output
        var query by mutableStateOf("")

        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = query,
                onQueryChanged = { newText ->
                    query = newText
                },
                installedAppsState = InstalledAppsState.Success(sampleApps),
                blockedApps = emptySet(),
                onItemClicked = { _, _ -> }
            )
        }

        composeTestRule.waitForIdle()

        // ACT: Find the TextField and type new text into it
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("query")

        composeTestRule.waitForIdle()

        // ASSERT: Check that the callback was triggered with the final text
        assertEquals("query", query)
    }

    @Test
    fun whenAppIsBlocked_checkboxIsChecked() {
        val blockedSet = setOf("com.app.one")

        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Success(sampleApps),
                blockedApps = blockedSet,
                onItemClicked = { _, _ -> }
            )
        }

        composeTestRule.waitForIdle()

        // ASSERT: The checkbox of blocked app is checked
        composeTestRule.onAllNodes(isToggleable())[0].assertIsOn()
    }

    @Test
    fun whenBlockedAppIsClicked_unblockCallbackIsTriggered() {
        var clickedPackage: String? = null
        var isChecked: Boolean? = null

        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Success(sampleApps),
                blockedApps = setOf("com.app.one"),
                onItemClicked = { pkg, checked ->
                    clickedPackage = pkg
                    isChecked = checked
                }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("App One").performClick()

        composeTestRule.waitForIdle()

        assertEquals("com.app.one", clickedPackage)
        assertEquals(false, isChecked)
    }

    @Test
    fun searchField_showsCurrentQuery() {
        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "abc",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Success(sampleApps),
                blockedApps = emptySet(),
                onItemClicked = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("SearchTextField").assert(hasText("abc"))
    }

    @Test
    fun whenStateChangesFromLoadingToSuccess_correctContentIsShown() {
        var state by mutableStateOf<InstalledAppsState>(InstalledAppsState.Loading)

        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = state,
                blockedApps = emptySet(),
                onItemClicked = { _, _ -> }
            )
        }

        // Initially shows loader
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()

        // Switch to Success
        state = InstalledAppsState.Success(sampleApps)
        composeTestRule.waitForIdle()

        // Now should show the list instead
        composeTestRule.onNodeWithTag("AppsList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoadingIndicator").assertDoesNotExist()
    }

    @Test
    fun whenSuccessStateWithEmptyList_showsEmptyList() {
        composeTestRule.setContent {
            AppsSelectionScreen(
                searchQuery = "",
                onQueryChanged = {},
                installedAppsState = InstalledAppsState.Success(emptyList()),
                blockedApps = emptySet(),
                onItemClicked = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("AppsList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppsList").onChildren().assertCountEquals(0)
    }
}