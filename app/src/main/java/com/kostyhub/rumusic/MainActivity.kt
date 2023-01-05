package com.kostyhub.rumusic

import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.ime
import com.kostyhub.rumusic.screens.*
import com.kostyhub.rumusic.ui.theme.RUMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Main()
        }
    }
}

object Model {

}

sealed class MainActivityUiState {
    object Empty   : MainActivityUiState()
    object Loading : MainActivityUiState()
    object Inlet   : MainActivityUiState()
}

@Preview(showBackground = true)
@Composable
fun Main() {
    RUMusicTheme {
        val viewModel by remember { mutableStateOf(Inlet.ViewModel()) }
        val model by remember { mutableStateOf(Inlet.Model(viewModel)) }
        Inlet.View(viewModel)

        //Log.d("dd", "${WindowInsets.Companion.ime.getBottom(LocalDensity.current)}")
        /*Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(10.dp)
        ) {
            TextField(value = "000", onValueChange = { })
            BasicTextField(value = "001", onValueChange = { })
            Spacer(Modifier.weight(1f))
            Button(onClick = {}) {
                Text(text = "Yeah Visible")
            }
        }*/
    }
}