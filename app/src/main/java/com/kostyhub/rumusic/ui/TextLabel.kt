package com.kostyhub.rumusic.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextLabel(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 44.sp) =
    Text(text,
        modifier = modifier,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold
    )

@Preview
@Composable
private fun DefaultPreview() = TextLabel("It's text label")