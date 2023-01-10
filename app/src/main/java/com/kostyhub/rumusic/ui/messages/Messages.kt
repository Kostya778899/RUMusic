package com.kostyhub.rumusic.ui.messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostyhub.rumusic.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Message {
    enum class Style(val color: Color, private val iconResourceId: Int) {
        Succeed(    Color(0xFFA7F0C5), R.drawable.icon_succeed),
        Warning(    Color(0xFFFED289), R.drawable.icon_warning),
        Error(      Color(0xFFF5A6A2), R.drawable.icon_error),
        Info(       Color(0xFF5CD2E8), R.drawable.icon_info);

        val icon @Composable get() = painterResource(iconResourceId)
    }

    abstract class Base(style: Style) {
        @Composable
        abstract fun Draw()
    }

    class Banner(
        private val style: Style,
        private val text: String,
    ) : Base(style) {
        @Composable
        override fun Draw() {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(style.color, RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(style.icon, null, Modifier.size(16.dp), Color.Unspecified)
                Text(text,
                    color = Color(0xFF0A1811),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                )
            }
        }

    }
    class Flag(
        private val style: Style,
        private val title: String,
        private val description: String,
    ) : Base(style) {
        @Composable
        override fun Draw() {
            Row(
                modifier = Modifier
                    .width(350.dp)
                    .background(style.color, RoundedCornerShape(8.dp))
                    .padding(16.dp, 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(style.icon, null, Modifier.size(24.dp), Color.Unspecified)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(title,
                        color = Color(0xFF0A1811),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                    )
                    Text(description,
                        color = Color(0xFF0A1811),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                    )
                }

            }
        }
    }
}

object Messages {
    sealed class UiState {
        object Empty: UiState()
    }

    class ViewModel {
        private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
        val uiState: StateFlow<UiState> = _uiState

        val messages = mutableStateListOf<Message.Base>(
            Message.Flag(Message.Style.Succeed, "Kostya", "lol, it's work!"),
            Message.Banner(Message.Style.Warning, "F* you!"),
            Message.Flag(Message.Style.Error, "Peta", "lol, it's not work"),
            Message.Flag(Message.Style.Info, "Jin", "you do this"),
            Message.Banner(Message.Style.Info, "Create an account"),
        )
    }

    @Composable
    fun View(viewModel: ViewModel) {
        var messages = remember { viewModel.messages }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.End
        ) {
            var visible by remember { mutableStateOf(true) }

            Button(onClick = { visible = !visible }, modifier = Modifier.fillMaxWidth()) {
                Text("Show?")
            }
            Spacer(Modifier.weight(1f))

            for (message in messages) {
                if (message is Message.Flag) {

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn() + slideInHorizontally { 1100 },
                        exit = fadeOut() + slideOutHorizontally { 1100 }
                    ) {
                        message.Draw()
                    }
                }
            }

            Box(
                modifier = Modifier
            ) {
                for (message in messages.asReversed()) {
                    if (message is Message.Banner) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn() + slideInVertically { 100 },
                            exit = fadeOut()
                        ) {
                            message.Draw()
                        }
                        break
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun DefaultPreview() {
    /*Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.End
    ) {
        Message.Flag(Message.Style.Succeed,
            title = "Good news, everyone",
            description = "Nothing to worry about, everything is going great!"
        ).Draw()
        Message.Flag(Message.Style.Info,
            title = "Good news, everyone",
            description = "Nothing to worry about, everything is going great!"
        ).Draw()
        Message.Flag(Message.Style.Warning,
            title = "Good news, everyone",
            description = "Nothing to worry about, everything is going great!"
        ).Draw()
        Message.Flag(Message.Style.Error,
            title = "Good news, everyone",
            description = "Nothing to worry about, everything is going great!"
        ).Draw()
        Message.Banner(Message.Style.Succeed, "This is a good").Draw()
        Message.Banner(Message.Style.Info, "This is a good").Draw()
        Message.Banner(Message.Style.Warning, "This is a good").Draw()
        Message.Banner(Message.Style.Error, "This is a good").Draw()
    }*/

    Messages.View(remember { mutableStateOf(Messages.ViewModel()) }.value)
}