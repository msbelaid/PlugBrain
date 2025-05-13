package app.matholck.android.ui.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.matholck.android.ui.theme.MathlockAppTheme
import app.matholck.android.ui.timer.presentation.TimerViewModel
import kotlin.getValue
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimerActivity : ComponentActivity() {
  private val viewModel: TimerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathlockAppTheme {
              TimerScreen(30){
                viewModel.refreshProgressiveDifficulty()
              }
            }
        }
    }
}
