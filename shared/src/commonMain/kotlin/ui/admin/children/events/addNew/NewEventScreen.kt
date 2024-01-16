package ui.admin.children.events.addNew

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(component: NewEventComponent) {

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState()


    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {

            Button(
                onClick = { showDatePicker = true},
                content = { Text("Select date") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (showDatePicker) {
                DatePickerDialog(
                    confirmButton = {  },
                    onDismissRequest = { showDatePicker = false
                    println(Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis ?: 0).toLocalDateTime(
                        TimeZone.currentSystemDefault()))
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            TimePicker(timePickerState, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}