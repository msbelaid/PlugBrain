package app.plugbrain.android.ui.challenges.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CountChallengeScreen(
    count: Int
) {
    Column {
        Text("Count objects to unlock apps")
        Row { }
        TextField(value = "", onValueChange = {})
        Button(onClick = {}) { }
    }
}

@Preview
@Composable
private fun CountChallengeScreenPreview() {
    CountChallengeScreen(10)
}
