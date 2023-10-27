package ui.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen(component: MainComponent) {
    // bottom bar with absence and settings and stuff
    Button(onClick = {
        Data.clearData()
    }) {
        Text("Clear shit")
    }
}