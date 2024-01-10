package ui.admin.children.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun EventsScreen(component: EventsComponent) {
    val events = component.events.subscribeAsState().value

    Column(Modifier.fillMaxSize()) {
        for (event in events) {
            ListItem(headlineContent = {
                Text(event.title)
            })

            Divider()
        }

    }
}