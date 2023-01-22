package com.kostyhub.rumusic.ui.messages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostyhub.rumusic.CMath
import com.kostyhub.rumusic.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextUBytes

object Message {
    enum class Style(val color: Color, private val iconResourceId: Int) {
        Succeed(    Color(0xFFA7F0C5), R.drawable.icon_succeed),
        Warning(    Color(0xFFFED289), R.drawable.icon_warning),
        Error(      Color(0xFFF5A6A2), R.drawable.icon_error),
        Info(       Color(0xFF5CD2E8), R.drawable.icon_info);

        val icon @Composable get() = painterResource(iconResourceId)
    }

    object Banner {
        @Composable
        fun Draw(style: Style, text: String) {
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
    object Flag {
        data class ViewModel(val style: Style, val title: String, val description: String) {
            var removed = false
            @OptIn(ExperimentalMaterialApi::class)
            val swipeableState = SwipeableState(0)
        }

        @Composable
        fun View(viewModel: ViewModel) {
            Row(
                modifier = Modifier
                    .width(350.dp)
                    .background(viewModel.style.color, RoundedCornerShape(8.dp))
                    .padding(16.dp, 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(viewModel.style.icon, null, Modifier.size(24.dp), Color.Unspecified)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        viewModel.title,
                        color = Color(0xFF0A1811),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                    )
                    Text(
                        viewModel.description,
                        color = Color(0xFF0A1811),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                    )
                }
            }
        }

        @OptIn(ExperimentalMaterialApi::class)
        @Composable
        fun ViewRemovableContainer(
            onRemoved: () -> Unit,
            content: @Composable BoxScope.() -> Unit,
        ) {
            var swipeableState = rememberSwipeableState(0)
            val endRight = 700f
            val anchors = mapOf(0f to 0, endRight to 1) /*by remember { mutableStateMapOf(0f to 0, endRight to 1) }*/
            var removed by remember { mutableStateOf(false) }

            if (!removed && swipeableState.offset.value >= endRight) {
                onRemoved()
                swipeableState = rememberSwipeableState(0)
                removed = true
            }

            Box(
                modifier = Modifier
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.5f) },
                        orientation = Orientation.Horizontal
                    )
            ) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                        .alpha(1 - CMath.normalize(0f, endRight, swipeableState.offset.value))
                ) {
                    content()
                    Text(removed.toString())
                }
            }
        }

        object Container {
            sealed class UiState {
                object Empty : UiState()
            }

            class ViewModel {
                private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
                val uiState: StateFlow<UiState> = _uiState

                val messages = mutableStateListOf<Flag.ViewModel>(
                    Flag.ViewModel(Style.Succeed, "Lexa say",    "Lol, It's work"),
                    Flag.ViewModel(Style.Warning, "Polina say",  "eawsrdtfygh jnh.."),
                    Flag.ViewModel(Style.Info,    "Sasha speak", "It's not 'LOL'"),
                )
            }

            @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
            @Composable
            fun View(viewModel: ViewModel) {
                /*val messages: List<@Composable LazyListScope.() -> Unit> = viewModel.messages.map { message ->
                    {
                        Box(
                            modifier = Modifier//.animateItemPlacement()
                        ) {
                            ViewRemovableContainer({ viewModel.messages.remove(message) }) {
                                Flag.View(message)
                            }
                        }
                    }
                }*/
                Text(viewModel.messages.size.toString())
                val swipeableStates = mutableListOf<SwipeableState<Int>>()
                for (i in viewModel.messages) {
                    swipeableStates.add(rememberSwipeableState(0))
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    itemsIndexed (viewModel.messages) { index, message ->
                        Box(
                            modifier = Modifier.animateItemPlacement()
                        ) {
                            //val swipeableState = rememberSwipeableState(0)
                            val endRight = 700f
                            val anchors = mapOf(0f to 0, endRight to 1) /*by remember { mutableStateMapOf(0f to 0, endRight to 1) }*/
                            //var removed by remember { mutableStateOf(false) }

                            if (!message.removed && message.swipeableState.offset.value >= endRight) {
                                message.removed = true
                                viewModel.messages.remove(message)
                            }

                            Box(
                                modifier = Modifier
                                    .swipeable(
                                        state = message.swipeableState,
                                        anchors = anchors,
                                        thresholds = { _, _ -> FractionalThreshold(0.5f) },
                                        orientation = Orientation.Horizontal
                                    )
                                    .offset {
                                        IntOffset(
                                            message.swipeableState.offset.value.roundToInt(),
                                            0
                                        )
                                    }
                                    .alpha(
                                        1 - CMath.normalize(
                                            0f,
                                            endRight,
                                            message.swipeableState.offset.value
                                        )
                                    )
                            ) {
                                Flag.View(message)
                                Text(message.removed.toString())
                            }
                        }
                        Text(index.toString(), modifier = Modifier.animateItemPlacement())
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun DefaultPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.End
    ) {
        val messagesViewModel by remember { mutableStateOf(Message.Flag.Container.ViewModel()) }

        Button(
            onClick = {
                messagesViewModel.messages.add(
                    Message.Flag.ViewModel(
                        style = Message.Style.Info,
                        title = "Message_${Random.Default.nextInt()}",
                        description = "Text ${Random.Default.nextInt().toString(2)}"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add") }
        Button(
            onClick = {
                messagesViewModel.messages.removeAt(0)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Remove") }
        Spacer(Modifier.weight(1f))
        Message.Flag.Container.View(messagesViewModel)
    }
}