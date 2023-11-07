package ui.main.children.settings

import Data
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.person.info.readInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui.RootComponent
import ui.login.LoginComponent
import ui.login.LoginScreen
import ui.main.DefaultMainComponent
import ui.main.MainComponent

@Composable
fun SettingsScreen(component: SettingsComponent) {

    val scope = rememberCoroutineScope()

    val res = runBlocking { readInfo() }

    if (res == null) {
        scope.launch { component.parent.snackbarHost.showSnackbar("There was an error retrieving user data.") }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Button(onClick = {
            Data.clearData()
            component.parent.parent.navigateTo(RootComponent.Config.Onboarding)
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
            Text(
                text = "Logout",
                style = TextStyle(color = Color.White, fontSize = 16.sp)
            )
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