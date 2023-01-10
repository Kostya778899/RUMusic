package com.kostyhub.rumusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.kostyhub.rumusic.screens.*
import com.kostyhub.rumusic.ui.theme.RUMusicTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    class Model(viewModel: Inlet.ViewModel)

    sealed class UiState {
        object Empty   : UiState()
        object Loading : UiState()
        object Inlet   : UiState()
    }

    class ViewModel {
        private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
        val uiState: StateFlow<UiState> = _uiState
    }

    @Composable
    fun View(viewModel: ViewModel) {
        RUMusicTheme {
            Inlet.View(remember { mutableStateOf(Inlet.ViewModel()) }.value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            View(remember { mutableStateOf(ViewModel()) }.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() =
    MainActivity().View(remember { mutableStateOf(MainActivity.ViewModel()) }.value)