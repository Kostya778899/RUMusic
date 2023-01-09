package com.kostyhub.rumusic.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.kostyhub.rumusic.KeyboardState
import com.kostyhub.rumusic.keyboardAsState

@Composable
fun NamedContainer(labelText: String, content: @Composable ColumnScope.() -> Unit) {
    val keyboardState by keyboardAsState()
    val labelAnimation = remember { Animatable(0f) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .background(Color(0xFF121212))
            .padding(24.dp, 36.dp, 24.dp, 26.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(keyboardState) {
            labelAnimation.animateTo(
                if (keyboardState == KeyboardState.Opened) 1f else 0f,
                tween(400)
            )
        }
        Row(horizontalArrangement = Arrangement.Center) {
            TextLabel(labelText,
                modifier = Modifier,
                fontSize = lerp(44.sp, 20.sp, labelAnimation.value)
            )
            Spacer(Modifier.fillMaxWidth(labelAnimation.value))
        }

        content()
    }
}