package ui.admin.children.hours

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import api.admin.absence.RequestedHourData
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import icons.AppIcons
import icons.appicons.Printer
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HoursScreen(component: HoursComponent) {
    Box(Modifier.fillMaxSize()) {
        val hours = component.hours.subscribeAsState().value

        var checked by remember { mutableStateOf(listOf<RequestedHourData>()) }

        // The LazyColumn will be our table. Notice the use of the weights below
        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            // Here is the header
            item {
                Row(Modifier.background(Color.Gray), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked.isNotEmpty(), onCheckedChange = {
                        checked = if (it) hours
                        else emptyList()
                    }, modifier = Modifier.size(20.dp))

                    TableCell(weight = .1f) {}
                    TableCell(weight = .3f) { Text("User") }
                    TableCell(weight = .5f) { Text("Hour") }
                }
            }
            // Here are all the lines of your table.
            items(hours) { event ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked.contains(event), onCheckedChange = {
                        checked = if (it) checked + event
                        else checked.filter { it != event }
                    }, modifier = Modifier.size(20.dp))

                    Spacer(Modifier.size(3.dp))

                    val time = Instant.fromEpochSeconds(event.hour.startTime).toLocalDateTime(TimeZone.currentSystemDefault())

                    TableCell(weight = .3f) { Text(event.user.firstName.toString()) }
                    TableCell(weight = .5f) { Text("Start Time: " + time.date.toString() + " " + time.time.toString()) }
                }
            }
        }

        val scope = rememberCoroutineScope()

        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp),
            onClick = { scope.launch { component.print(checked) } },
            icon = {
                Icon(AppIcons.Printer, "Print")
            }, text = {
                Text("Print")
            }
        )
    }
}

@Composable
fun RowScope.TableCell(
    modifier: Modifier = Modifier,
    weight: Float,
    content: @Composable () -> Unit
) {
    Box(Modifier
        .border(Dp.Hairline, Color.Gray)
        .weight(weight)
        .padding(4.dp)
        .then(modifier)
    ) { content() }
}