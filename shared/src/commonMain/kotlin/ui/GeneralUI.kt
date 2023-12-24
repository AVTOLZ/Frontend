package ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.avt.app.MR
import dev.icerock.moko.resources.compose.painterResource

object GeneralUI {
    @Composable
    fun InputTextField(value: String, onValueChange: (String) -> Unit, label: String, passwordField: Boolean = false, modifier: Modifier = Modifier) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            visualTransformation = if (passwordField) { if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation() } else VisualTransformation.None,
            keyboardOptions = if (passwordField) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .then(modifier),

            trailingIcon = {
                if (passwordField) {
                    val image = if (passwordVisible)
                        MR.images.visibility
                    else MR.images.visiblity_off

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(image), null)
                    }
                }
            }
        )
    }
}