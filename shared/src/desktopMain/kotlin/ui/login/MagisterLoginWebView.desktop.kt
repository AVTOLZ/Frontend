package ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.sun.javafx.webkit.WebConsoleListener
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.web.WebErrorEvent
import javafx.scene.web.WebEvent
import javafx.scene.web.WebView
import java.awt.Taskbar
import java.util.concurrent.Future
import javax.swing.JPanel

@Suppress("SetJavaScriptEnabled")
@Composable
actual fun MagisterLoginWebView(getLoginUrl: () -> String, onUrlLoad: (url: String) -> Boolean) {
    SelectionContainer {
        Column {
            val loginUrl = remember { getLoginUrl() }

            Text("Open the following link: $loginUrl")

            var enteredCode by remember { mutableStateOf("") }
            TextField(enteredCode, onValueChange = { enteredCode = it })

            Button(onClick = {
                onUrlLoad(enteredCode)
            }) {
                Text("Submit")
            }
        }
    }
}