package ui.main.children.presence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import api.person.absence.availability.AvailabilityItem
import api.person.absence.availability.PresenceType
import api.person.absence.present.announcePresence
import api.person.absence.requestHours.requestHours
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlin.time.DurationUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TimetableItems(
    component: PresenceComponent,
    page: Int,
    timesShown: IntRange,
    dpPerHour: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val now = remember { Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")) }

        val startOfWeekDate = getStartOfWeekFromDay(page, 350, now)
        val selectedDayDate = startOfWeekDate.plus(page % 7, DateTimeUnit.DAY)

        // Get the time as Instant of the top of the timetable
        val timeTop = selectedDayDate.atStartOfDayIn(TimeZone.of("Europe/Amsterdam")).plus(timesShown.first(), DateTimeUnit.HOUR)

        val timetable = component.timetable.subscribeAsState()
        val scope = rememberCoroutineScope()

        component.getTimetableForWeek(timetable.value, startOfWeekDate).getAgendaForDay(page % 7)
            .forEach { availabilityItem ->
                val height = dpPerHour * (Instant.fromEpochSeconds(availabilityItem.endTime) -Instant.fromEpochSeconds(availabilityItem.startTime)).toDouble(DurationUnit.HOURS).toFloat()
                var distanceAfterTop = dpPerHour * (Instant.fromEpochSeconds(availabilityItem.startTime) - timeTop).toDouble(DurationUnit.HOURS).toFloat()
                if (distanceAfterTop < 0.dp) distanceAfterTop = 0.dp

                TimetableItem(item = availabilityItem, modifier = Modifier.padding(start = 40.5.dp, top = distanceAfterTop).height(height), onError = { scope.launch { component.parent.snackbarHost.showSnackbar(it) }}) {
                    component.openTimeTableItem(availabilityItem)
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItem(item: AvailabilityItem, modifier: Modifier, onError: (String) -> Unit, onClick: () -> Unit) {
    val supportingText = mutableListOf<String>("prachtige description")

    var checkedPresent by remember { mutableStateOf( item.presentType == PresenceType.PRESENT) }
    var checkedAbsence by remember { mutableStateOf( item.presentType == PresenceType.ABSENCE ) }

    ListItem(
        modifier = modifier
            .clickable(onClick = onClick)
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineContent = { Text("AVT IS GEWELDIG ") }, // TODO
        supportingContent = {
            /* TODO: Description here or something */
        },
        trailingContent = {
            Row {
                Checkbox(
                    checked = checkedPresent,
                    enabled = !item.approved,
                    onCheckedChange = {

                        checkedPresent = it

                        runBlocking {
                            val res = announcePresence(item.id, !it)

                            if (res == HttpStatusCode.Conflict) {
                                onError("The absence request has been approved \n" +
                                        "to remove contact administrator")
                                checkedPresent = !it
                                return@runBlocking
                            }

                            if (res != HttpStatusCode.OK) {
                                onError("There was an error communicating with the server")
                                checkedPresent = !it
                                return@runBlocking
                            }
                        }

                        if ((checkedPresent == it).and(it)) {
                            checkedAbsence = false
                        }
                    }
                )
                /* TODO: Abel place checkmarks here */
                Checkbox(
                    checked = checkedAbsence,
                    enabled = !item.approved,
                    onCheckedChange = {

                        checkedAbsence = it

                        runBlocking {
                            val res = requestHours(item.id, !it)

                            if (res == HttpStatusCode.Conflict) {
                                onError("The absence request has been approved \n" +
                                        "to remove contact administrator")
                                checkedAbsence = !it
                                return@runBlocking
                            }

                            if (res != HttpStatusCode.OK) {
                                onError("There was an error communicating with the server.")
                                checkedAbsence = !it
                                return@runBlocking
                            }
                        }

                        if ((checkedAbsence == it).and(it)) {
                            checkedPresent = false
                        }
                    }
                )
            }
        },
    )
}

@Suppress("UnnecessaryComposedModifier")
fun Modifier.topBottomRectBorder(
    width: Dp = Dp.Hairline,
    brush: Brush = SolidColor(Color.LightGray)
): Modifier = composed(
    factory = {
        this.then(
            Modifier.drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawLine(
                        brush,
                        Offset(0f, 0f - width.value),
                        Offset(size.width, 0f - width.value)
                    )
                    drawLine(
                        brush,
                        Offset(0f, size.height - width.value),
                        Offset(size.width, size.height - width.value)
                    )
                }
            }
        )
    },
    inspectorInfo = debugInspectorInfo {
        name = "border"
        properties["width"] = width
        if (brush is SolidColor) {
            properties["color"] = brush.value
            value = brush.value
        } else {
            properties["brush"] = brush
        }
        properties["shape"] = RectangleShape
    }
)