package ui.main.children.presence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
            .forEach { agendaItemWithAbsence ->
                val agendaItem = agendaItemWithAbsence.agendaItem

                val startTime = agendaItem.start.magisterDateToInstant
                val endTime = agendaItem.einde.magisterDateToInstant

                val height = dpPerHour * (endTime - startTime).toDouble(DurationUnit.HOURS).toFloat()
                var distanceAfterTop = dpPerHour * (startTime - timeTop).toDouble(DurationUnit.HOURS).toFloat()
                if (distanceAfterTop < 0.dp) distanceAfterTop = 0.dp

                TimetableItem(item = agendaItemWithAbsence, modifier = Modifier.padding(start = 40.5.dp, top = distanceAfterTop).height(height)) {
                    component.openTimeTableItem(agendaItemWithAbsence)
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItem(item: AgendaItemWithAbsence, modifier: Modifier, onClick: () -> Unit) {
    val agendaItem = item.agendaItem
    val absence = item.absence

    val startTime = agendaItem.start.magisterDateToInstant.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    val endTime = agendaItem.einde.magisterDateToInstant.toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

    val supportingText = mutableListOf<String>()

    if (!agendaItem.location.isNullOrEmpty()) supportingText.add(agendaItem.location!!)
    supportingText.add(
        startTime.hour.toString().padStart(2, '0') + ":" +
                startTime.minute.toString().padStart(2, '0') + " - " +
                endTime.hour.toString().padStart(2, '0') + ":" +
                endTime.minute.toString().padStart(2, '0')
    )

    if (!agendaItem.content.isNullOrEmpty()) supportingText.add(
        agendaItem.content!!
    )

    ListItem(
        modifier = modifier
            .clickable(onClick = onClick)
            .topBottomRectBorder(brush = SolidColor(MaterialTheme.colorScheme.outline)),
        headlineText = { Text(agendaItem.description ?: "") },
        supportingText = {
            HtmlView(
                supportingText.joinToString(" • "),
                maxLines = 1,
            )
        },
        leadingContent = {
            if (agendaItem.fromPeriod != null) Text(agendaItem.fromPeriod!!.toString(), modifier = Modifier.padding(2.dp))
            else Spacer(modifier = Modifier.size(16.dp))
        },
        trailingContent = {
            if (absence != null) AbsenceBox(absence)
        },
        colors = ListItemDefaults.colors(
            containerColor = if (agendaItem.getStatus().isCancelled) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.inverseOnSurface,
        ),
    )
}