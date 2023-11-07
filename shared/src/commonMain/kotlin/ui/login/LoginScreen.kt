package ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import api.accounts.login
import api.accounts.magisterLogin
import dev.avt.app.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.tiebe.magisterapi.api.account.LoginFlow
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui.GeneralUI
import ui.RootComponent

@Composable
fun LoginScreen(component: LoginComponent) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        var magisterScreenVisible by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

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

            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }


            GeneralUI.InputTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )

            GeneralUI.InputTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password"
            )


            Button(
                onClick = {
                    val success = runBlocking {
                        login(username, password)
                    }

                    when (success) {
                        null -> scope.launch { component.parent.snackbarHost.showSnackbar("There was an error logging in.") }
                        true -> component.parent.navigateTo(RootComponent.Config.Main)
                        false -> errorMessage = "Invalid username or password."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    component.parent.clearStack(RootComponent.Config.Register)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Sign up")
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
                val code = getCode(url) ?: return@MagisterLoginWebView false

                // TODO: make this async with loading indicator

                val success = runBlocking {
                    val tokens = LoginFlow.exchangeTokens(code, loginUrl.codeVerifier)

                    magisterLogin(tokens.refreshToken)
                }

                when (success) {
                    null -> {
                        scope.launch { component.parent.snackbarHost.showSnackbar("there was an error connecting to the server") }
                        return@MagisterLoginWebView false
                    }
                    true -> {
                        component.parent.navigateTo(RootComponent.Config.Main)
                    }
                    false -> {
                        errorMessage = "Error while signing into Magister, please try again."
                        magisterScreenVisible = false
                    }
                }

                return@MagisterLoginWebView success
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
