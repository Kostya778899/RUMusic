package com.kostyhub.rumusic.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kostyhub.rumusic.KeyboardState
import com.kostyhub.rumusic.keyboardAsState
import com.kostyhub.rumusic.ui.TextField
import com.kostyhub.rumusic.ui.NamedContainer
import com.kostyhub.rumusic.ui.buttons.Button
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Inlet {
    class Model(viewModel: ViewModel)

    sealed class UiState {
        object Empty                                         : UiState()
        object Loading                                       : UiState()
        class  Login(val errors: List<String> = listOf())    : UiState()
        class  Register(val errors: List<String> = listOf()) : UiState()
        class  Error(val message: String)                    : UiState()
    }

    class ViewModel {
        private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
        val uiState: StateFlow<UiState> = _uiState

        init { showLoginPage() }

        fun showLoginPage() { _uiState.value = UiState.Login() }
        fun showRegisterPage() { _uiState.value = UiState.Register() }

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
            is UiState.Empty -> Text("NULL")
            is UiState.Loading -> Text("Load...")
            is UiState.Login -> NamedContainer("Login") {
                var username by remember { mutableStateOf(TextFieldValue("")) }
                var password by remember { mutableStateOf(TextFieldValue("")) }

                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    TextField(username, { username = it }, "Username")
                    TextField(password, { password = it }, "Password")
                }
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Button({ viewModel.login(username.text, password.text) }, "Login")
                    Button(
                        { viewModel.showRegisterPage() }, "Register",
                        color = Color(0xFF3700B3)
                    )
                    if (state.errors.isNotEmpty()) state.errors.forEach { Text(it) }
                }
            }
            is UiState.Register -> NamedContainer("Register") {
                val keyboardState by keyboardAsState()
                var fullName by remember { mutableStateOf(TextFieldValue("")) }
                var email by remember { mutableStateOf(TextFieldValue("")) }
                var phone by remember { mutableStateOf(TextFieldValue("")) }
                var password by remember { mutableStateOf(TextFieldValue("")) }
                var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    TextField(fullName, { fullName = it }, "Full Name")
                    TextField(email, { email = it }, "E-mail")
                    TextField(phone, { phone = it }, "Phone")
                    Row(Modifier.fillMaxWidth()) {
                        TextField(password, { password = it }, "Password",
                            Modifier.weight(1f), PasswordVisualTransformation())
                        Spacer(Modifier.width(18.dp))
                        TextField(confirmPassword, { confirmPassword = it }, "Confirm Password",
                            Modifier.weight(1f), PasswordVisualTransformation())
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    if (keyboardState == KeyboardState.Closed) {
                        Button({
                            viewModel.register(
                                fullName.text,
                                email.text,
                                phone.text,
                                password.text,
                                confirmPassword.text,
                            )
                        }, "Register")
                        Button(
                            { viewModel.showLoginPage() }, "Have account? Sign In",
                            color = Color(0xFF3700B3)
                        )
                    }
                    if (state.errors.isNotEmpty()) state.errors.forEach { Text(it) }
                }
            }
            is UiState.Error -> Text("Error: ${state.message}")
        }
    }
}

//region Previews
/*@Preview
@Composable
private fun DefaultPreview() { }*/
@Preview
@Composable
private fun LoginPreview() {
    val viewModel by remember { mutableStateOf(Inlet.ViewModel()) }
    val model by remember { mutableStateOf(Inlet.Model(viewModel)) }
    Inlet.View(viewModel)
}
@Preview
@Composable
private fun RegisterPreview() {
    val viewModel by remember { mutableStateOf(Inlet.ViewModel()) }
    val model by remember { mutableStateOf(Inlet.Model(viewModel)) }
    viewModel.showRegisterPage()
    Inlet.View(viewModel)
}
//endregion