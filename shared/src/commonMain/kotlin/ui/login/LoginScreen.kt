package ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.avt.app.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*

@Composable
fun LoginScreen(component: LoginComponent) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        var magisterScreenVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            emailFocusRequester.requestFocus()
                        }
                    }
                    .focusRequester(emailFocusRequester)
                    .onPreviewKeyEvent { event ->
                        if (event.key == Key.Tab && !event.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else {
                            false
                        }
                    }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            passwordFocusRequester.requestFocus()
                        }
                    }
                    .focusRequester(passwordFocusRequester)
                    .onPreviewKeyEvent { event ->
                        if (event.key == Key.Tab && event.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Up)
                            true
                        } else {
                            false
                        }
                    }
            )

            Button(
                onClick = { /* Handle login button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = { magisterScreenVisible = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Image(
                    painterResource(MR.images.magister),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).padding(end = 8.dp)
                )
                Text("Login with Magister")
            }
        }

        AnimatedVisibility(visible = magisterScreenVisible, modifier = Modifier.fillMaxSize()) {
            var loginUrl by remember { mutableStateOf(LoginFlow.createAuthURL()) }

            MagisterLoginWebView(getLoginUrl = {
                loginUrl = LoginFlow.createAuthURL()
                loginUrl.url
            }) { url ->
                val code = getCode(url)

                if (code == null) {

                }

                return@MagisterLoginWebView true
            }
        }
    }
}

fun getCode(inputUrl: String): String? {
    val url = inputUrl.replace("#", "?")

    if (url.startsWith("m6loapp://oauth2redirect")) {
        val parameters = Url(url).parameters

        if (parameters.contains("error")) {
            return null
        } else {
            val code = parameters["code"] ?: return null

            return code
        }
    }

    return null
}