package ui.verify

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun VerificationScreen(component: VerificationComponent) {
    Column(Modifier.fillMaxSize()) {
        var verificationCode by remember { mutableStateOf("") }

        TextField(
            verificationCode,
            { verificationCode = it },
            placeholder = { Text("Verification code") },
        )

        Button(onClick = {
            component.verify(verificationCode)
        }) {
            Text("Verify")
        }
    }


}