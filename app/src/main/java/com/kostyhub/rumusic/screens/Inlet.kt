package com.kostyhub.rumusic.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Inlet {
    class Model(viewModel: ViewModel) { }

    sealed class InletUiState {
        object Empty                                         : InletUiState()
        object Loading                                       : InletUiState()
        class  Login(val errors: List<String> = listOf())    : InletUiState()
        class  Register(val errors: List<String> = listOf()) : InletUiState()
        class  Error(val message: String)                    : InletUiState()
    }

    class ViewModel {
        private val _uiState = MutableStateFlow<InletUiState>(InletUiState.Empty)
        val uiState: StateFlow<InletUiState> = _uiState

        init { showLoginPage() }

        fun showLoginPage() { _uiState.value = InletUiState.Login() }
        fun showRegisterPage() { _uiState.value = InletUiState.Register() }

        fun login(username: String, password: String) { }
        fun register(
            fullName: String,
            email: String,
            phone: String,
            password: String,
            confirmPassword: String,
        ) { }
    }

    @Composable
    fun View(viewModel: ViewModel) {
        when (val state = viewModel.uiState.collectAsState().value) {
            is InletUiState.Empty -> Text("NULL")
            is InletUiState.Loading -> Text("Load...")
            is InletUiState.Login ->
                CContainer("Login") {
                    var username by remember { mutableStateOf(TextFieldValue("")) }
                    var password by remember { mutableStateOf(TextFieldValue("")) }

                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        CTextField(username, { username = it }, "Username")
                        CTextField(password, { password = it }, "Password")
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        CButton({ viewModel.login(username.text, password.text) }, "Login")
                        CButton({ viewModel.showRegisterPage() }, "Register",
                            color = Color(0xFF3700B3))
                        if (state.errors.isNotEmpty()) state.errors.forEach { Text(it) }
                    }
                }
            is InletUiState.Register ->
                CContainer("Register") {
                    var fullName        by remember { mutableStateOf(TextFieldValue("")) }
                    var email           by remember { mutableStateOf(TextFieldValue("")) }
                    var phone           by remember { mutableStateOf(TextFieldValue("")) }
                    var password        by remember { mutableStateOf(TextFieldValue("")) }
                    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        CTextField(fullName,        { fullName = it },        "Full Name")
                        CTextField(email,           { email = it },           "E-mail")
                        CTextField(phone,           { phone = it },           "Phone")
                        CTextField(password,        { password = it },        "Password")
                        CTextField(confirmPassword, { confirmPassword = it }, "Confirm Password")
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        CButton({
                            viewModel.register(
                                fullName.text,
                                email.text,
                                phone.text,
                                password.text,
                                confirmPassword.text,
                            )
                        }, "Register")
                        CButton({ viewModel.showLoginPage() }, "Have account? Sign In",
                            color = Color(0xFF3700B3))
                        if (state.errors.isNotEmpty()) state.errors.forEach { Text(it) }
                    }
                }

            is InletUiState.Error -> Text("Error: ${state.message}")
        }
    }

    //region CUi
    @Composable
    fun CContainer(name: String, content: @Composable ColumnScope.() -> Unit) {
        var height by remember { mutableStateOf(0.dp) }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .background(Color(0xFF121212))
                .padding(24.dp, 36.dp, 24.dp, 26.dp)
                .onGloballyPositioned { coordinates ->
                    height = coordinates.size.height.dp
                    Log.d("-----------------------", height.value.toString())
                },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(name, color = Color.White, fontSize = 44.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun CTextField(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
        placeholder: String
    ) {
        val shape = RoundedCornerShape(20.dp)
        val textColor = Color(0xDEFFFFFF)
        val keyboardController = LocalSoftwareKeyboardController.current
        var isFocused by remember { mutableStateOf(false) }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
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
            cursorBrush = SolidColor(textColor),
            decorationBox = { innerTextField ->
                Box {

                    innerTextField()
                    if (value.text.isEmpty()) {
                        Text(placeholder, color = textColor, fontSize = 15.sp)
                    }
                }
            }
        )
        //if (isFocused) Spacer(Modifier.height(25.dp))
    }
    @Composable
    fun CButton(
        onClick: () -> Unit,
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color(0xFF6200EE),
        textColor: Color = Color.White
    ) {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(38.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = textColor
            ),
            contentPadding = PaddingValues(10.dp)
        ) { Text(text) }
    }
    //endregion

    /*data class UserData(
        var fullName: String,
        var email: String,
        var phone: String,
        var password: String,
    )*/
}

@Preview
@Composable
private fun DefaultPreview() {
    val viewModel by remember { mutableStateOf(Inlet.ViewModel()) }
    val model by remember { mutableStateOf(Inlet.Model(viewModel)) }
    viewModel.showRegisterPage()
    Inlet.View(viewModel)
}