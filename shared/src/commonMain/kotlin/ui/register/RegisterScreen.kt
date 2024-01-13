package ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.accounts.register
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ui.GeneralUI.InputTextField

@Composable
fun RegisterScreen(component: RegisterComponent) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var errorString by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
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

            Text(
                text = errorString,
                color = Color.Red,
                modifier = Modifier
                    .padding(bottom = 5.dp),
                fontSize = 25.sp
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
                label = "Password",
                passwordField = true
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

            val regex = """^[0-9]*$""".toRegex()

            InputTextField(
                value = studentId,
                onValueChange = {
                    if (regex.matches(it)) {
                        studentId = it
                    }
                },
                label = "Student ID"
            )

            Button(
                onClick = {
                    GlobalScope.launch {
                        val success = register(username, password, email, firstName, lastName, studentId.toInt())

                        if (success == null) {
                            errorString = "This username or email is already in use."
                        }

                        if (success == true) {
                            component.parent.clearStack(component.parent.getInitialConfiguration())
                        }

                        if (success == false) {
                            scope.launch { component.parent.snackbarHost.showSnackbar("There was a connection error with the server, please try again later") }
                        }
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
