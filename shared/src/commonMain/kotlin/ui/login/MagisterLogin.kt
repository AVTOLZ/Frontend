package ui.login

import androidx.compose.runtime.*
import api.accounts.magisterLogin
import dev.tiebe.magisterapi.api.account.LoginFlow
import kotlinx.coroutines.launch

@Composable
fun MagisterLoginScreen(component: MagisterLoginComponent) {
    val scope = rememberCoroutineScope()

    var loginUrl by remember { mutableStateOf(LoginFlow.createAuthURL()) }

    MagisterLoginWebView(getLoginUrl = {
        loginUrl = LoginFlow.createAuthURL()
        loginUrl.url
    }) { url ->
        val code = getCode(url)
        if (code == null) {
            scope.launch {
                component.parent.snackbarHost.showSnackbar("Error while signing into Magister, please try again.")
                component.parent.clearStack(component.parent.getInitialConfiguration())
            }

            return@MagisterLoginWebView false
        }

        scope.launch {
            val tokens = LoginFlow.exchangeTokens(code, loginUrl.codeVerifier)

            val success = magisterLogin(tokens.refreshToken)

            when (success) {
                true -> {
                    component.parent.clearStack(component.parent.getInitialConfiguration())
                }
                false -> {
                    component.parent.snackbarHost.showSnackbar("Error while signing into Magister, please try again.")
                }
                null -> {
                    component.parent.snackbarHost.showSnackbar("There was an error connecting to the server")
                }
            }

        }

        return@MagisterLoginWebView true
    }
}