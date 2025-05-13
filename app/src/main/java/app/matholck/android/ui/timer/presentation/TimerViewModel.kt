package app.matholck.android.ui.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.datastore.DataStoreManager
import kotlinx.coroutines.launch

class TimerViewModel(
  private val dataStoreManager: DataStoreManager,
) : ViewModel() {

  fun refreshProgressiveDifficulty() {
    viewModelScope.launch {
      dataStoreManager.updateProgressiveDifficulty(0)
    }
  }
}
