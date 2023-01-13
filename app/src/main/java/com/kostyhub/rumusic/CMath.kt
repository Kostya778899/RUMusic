package com.kostyhub.rumusic

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

object CMath {
    @Stable
    fun normalize(start: Float, stop: Float, fraction: Float) = (fraction-start)/(stop-start)
}