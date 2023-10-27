package ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.accounts.register
import kotlinx.coroutines.runBlocking
import ui.GeneralUI.InputTextField
import ui.RootComponent

@Composable
fun RegisterScreen(component: RegisterComponent) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

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
                text = "Sign up",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InputTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            InputTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )

            InputTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password"
            )

            InputTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First name"
            )

            InputTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last name"
            )

            Button(
                onClick = {
                    val success = runBlocking {
                        register(username, password, email, firstName, lastName)
                    }

                    if (success) {
                        component.parent.navigateTo(RootComponent.Config.Verify)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Register")
            }
        }
    }
}