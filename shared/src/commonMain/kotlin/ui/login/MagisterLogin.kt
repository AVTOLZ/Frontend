package ui.login

import androidx.compose.runtime.*
import api.accounts.magisterLogin
import dev.tiebe.magisterapi.api.account.LoginFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui.RootComponent

@Composable
fun MagisterLoginScreen(component: MagisterLoginComponent) {
    val scope = rememberCoroutineScope()

    var loginUrl by remember { mutableStateOf(LoginFlow.createAuthURL()) }

    MagisterLoginWebView(getLoginUrl = {
        loginUrl = LoginFlow.createAuthURL()
        loginUrl.url
    }) { url ->
        val code = getCode(url) ?: return@MagisterLoginWebView false

        val success = runBlocking {
            val tokens = LoginFlow.exchangeTokens(code, loginUrl.codeVerifier)

            magisterLogin(tokens.refreshToken)
        }

        when (success) {
            true -> {
                component.parent.navigateTo(RootComponent.Config.Main)
            }
            false -> {
                scope.launch { component.parent.snackbarHost.showSnackbar("Error while signing into Magister, please try again.") }
                return@MagisterLoginWebView false
            }
            null -> {
                scope.launch { component.parent.snackbarHost.showSnackbar("There was an error connecting to the server") }
                return@MagisterLoginWebView false
            }
        }

        return@MagisterLoginWebView success
    }
}