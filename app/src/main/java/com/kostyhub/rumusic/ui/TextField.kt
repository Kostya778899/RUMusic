package com.kostyhub.rumusic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val shape = RoundedCornerShape(20.dp)
    val textColor = Color(0xDEFFFFFF)
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Transparent)
            .border(2.dp, Color(0xFFBB86FC), shape)
            .padding(11.dp)
            .onFocusEvent { focusState -> isFocused = focusState.isFocused },
        textStyle = TextStyle(textColor),
        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        singleLine = true,
        visualTransformation = /*PasswordVisualTransformation()*/visualTransformation,
        cursorBrush = SolidColor(textColor),
        decorationBox = { innerTextField ->
            Box {
                innerTextField()
                if (value.text.isEmpty()) Text(placeholder, color = textColor, fontSize = 15.sp)
            }
        }
    )
    //if (isFocused) Spacer(Modifier.height(25.dp))
}