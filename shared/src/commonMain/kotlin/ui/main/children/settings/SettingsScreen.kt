package ui.main.children.settings

import Data
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.person.info.readInfo
import api.person.magister.linkMagisterAccount
import dev.tiebe.magisterapi.api.account.LoginFlow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ui.RootComponent
import ui.login.MagisterLoginWebView
import ui.login.getCode

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val scope = rememberCoroutineScope()

    val res = scope.launch {
        if (readInfo() == null) {
            component.parent.snackbarHost.showSnackbar("There was an error retrieving user data.")
        }
    }

    var magisterScreenVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorMessage.isNotBlank()) {
            androidx.compose.material3.Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Card(
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ){
            Column(modifier = Modifier.padding(16.dp)) {
                CustomText(title = "Username", value = Data.username ?: "")
                CustomText(title = "Voornaam", value = Data.userFirstname ?: "")
                CustomText(title = "Achternaam", value = Data.userLastname ?: "")
                CustomText(title = "Rang", value = Data.userRankString ?: "")
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    Data.clearData()
                    component.parent.parent.navigateTo(RootComponent.Config.Onboarding)
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Logout",
                    style = TextStyle(color = Color.White, fontSize = 16.sp)
                )
            }

            Spacer(Modifier.width(5.dp))

            Button(
                onClick = {
                    magisterScreenVisible = true
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Link with Magister",
                    style = TextStyle(color = Color.White, fontSize = 16.sp)
                )
            }
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

            MainScope().launch {
                val tokens = LoginFlow.exchangeTokens(code, loginUrl.codeVerifier)

                val success = linkMagisterAccount(tokens.refreshToken)

                when (success) {
                    true -> {
                        scope.launch { component.parent.snackbarHost.showSnackbar("Successfully linked your Magister account.") }
                        magisterScreenVisible = false
                    }
                    false -> {
                        errorMessage = "Error while signing into Magister, please try again."
                        magisterScreenVisible = false
                    }
                }
            }

            return@MagisterLoginWebView false
        }
    }
}

@Composable
fun CustomText(title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = Color.Black, fontSize = 16.sp)
    }
}