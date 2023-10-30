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
import api.person.absence.availability.HourStatus
import api.person.absence.requestHours.HourRequestType
import api.person.absence.requestHours.requestHours
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
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

        component.getTimetableForWeek(timetable.value, startOfWeekDate).getAgendaForDay(page % 7)
            .forEach { availabilityItem ->
                val height = dpPerHour * (Instant.fromEpochSeconds(availabilityItem.endTime) -Instant.fromEpochSeconds(availabilityItem.startTime)).toDouble(DurationUnit.HOURS).toFloat()
                var distanceAfterTop = dpPerHour * (Instant.fromEpochSeconds(availabilityItem.startTime) - timeTop).toDouble(DurationUnit.HOURS).toFloat()
                if (distanceAfterTop < 0.dp) distanceAfterTop = 0.dp

                TimetableItem(item = availabilityItem, modifier = Modifier.padding(start = 40.5.dp, top = distanceAfterTop).height(height)) {
                    component.openTimeTableItem(availabilityItem)
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItem(item: AvailabilityItem, modifier: Modifier, onClick: () -> Unit) {
    val supportingText = mutableListOf<String>("prachtige description")

    // TODO implement approved time hours

    var checked by remember { mutableStateOf(item.status == HourStatus.Requested) }

    ListItem(
        modifier = modifier
            .clickable(onClick = onClick)
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineContent = { Text("AVT IS GEWELDIG ") }, // TODO
        supportingContent = {
            /* TODO: Description here or something */
        },
        trailingContent = {
            /* TODO: Abel place checkmarks here */
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it

                    val requestType: HourRequestType = if (it) {
                        HourRequestType.absent
                    } else {
                        HourRequestType.nothing
                    }

                    runBlocking {
                        val res = requestHours(item.id, requestType)
                        if (!res) {
                            checked = !it
                        }
                    }
                }
            )
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