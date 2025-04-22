package app.matholck.android.ui.challenges.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.matholck.android.repository.QuestionsRepository
import app.matholck.android.repository.model.Question
import dz.univ.usto.mathlock.datastore.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArithChallengeViewModel(
  private val dataStoreManager: DataStoreManager,
  private val questionsRepository: QuestionsRepository,
) : ViewModel() {
  private val _question: MutableStateFlow<Question?> = MutableStateFlow(null)
  val question: StateFlow<Question?> = _question

  init {
    generateQuestion()
  }

  private fun generateQuestion() {
    viewModelScope.launch {
      questionsRepository.generateQuestion().collect {
        _question.value = it
      }
    }
  }

  fun unblockApps() {
    viewModelScope.launch {
      dataStoreManager.updateBlockAppsToggle(false)
    }
  }
}
