package ui.main.children.settings

import Data
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.person.info.AVTRanks
import api.person.info.readInfo

@Composable
fun SettingsScreen(component: SettingsComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Username: " + Data.username
        )
        Text(
            text = "Voornaam: " + Data.userFirstname
        )
        Text(
            text = "Achternaam: " + Data.userLastname
        )
        Text(
            text = "Rang: " + Data.userRankString
        )
        Button(onClick = {
            Data.clearData()
        },
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Text(
                text = "Logout"
            )
        }
    }
}