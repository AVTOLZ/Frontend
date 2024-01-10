package ui.admin.children.events

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun EventsScreen(component: EventsComponent) {
    val events = component.events.subscribeAsState().value

    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        // Here is the header
        item {
            Row(Modifier.background(androidx.compose.ui.graphics.Color.Gray)) {
                TableCell(text = "Title", weight = column1Weight)
                TableCell(text = "Start Time", weight = column2Weight)
            }
        }
        // Here are all the lines of your table.
        items(events) { event ->
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = event.title, weight = column1Weight)
                TableCell(text = event.startTime.toString(), weight = column2Weight)
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(Dp.Hairline, Color.Gray)
            .weight(weight)
            .padding(4.dp)
    )
}