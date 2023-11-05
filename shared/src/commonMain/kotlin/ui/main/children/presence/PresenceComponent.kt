package ui.main.children.presence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.rememberCoroutineScope
import api.person.absence.availability.AvailabilityItem
import api.person.absence.availability.readAvailability
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.absence.AbsenceFlow.getAbsences
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.*
import kotlinx.datetime.*
import ui.componentCoroutineScope
import ui.main.MainComponent
import ui.main.MenuItemComponent
import kotlin.math.floor

val days = listOf(
    "mon",
    "tue",
    "wed",
    "thu",
    "fri",
    "sat",
    "sun"
)

interface PresenceComponent: MenuItemComponent {
    val now: Value<LocalDateTime>
    val firstDayOfWeek get() = now.value.date.minus(now.value.date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    val amountOfDays get() = 1000

    val currentPage: Value<Int>

    val timetable: Value<List<AvailabilityItem>>

    val openedTimetableItem: Value<Pair<Boolean, AvailabilityItem?>>

    val selectedWeek: Value<Int>

    val isRefreshingTimetable: Value<Boolean>

    fun changeDay(day: Int)

    fun refreshTimetable(from: LocalDate, to: LocalDate)

    fun refreshSelectedWeek() {
        refreshTimetable(
            firstDayOfWeek.plus(
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            ),
            firstDayOfWeek.plus(
                selectedWeek.value * 7,
                DateTimeUnit.DAY
            ).plus(days.size, DateTimeUnit.DAY)
        )
    }

    fun getTimetableForWeek(timetable: List<AvailabilityItem>, startOfWeekDate: LocalDate): List<AvailabilityItem> {
        return timetable.filter {
            it.startDateTime.date in startOfWeekDate..startOfWeekDate.plus(
                6,
                DateTimeUnit.DAY
            )
        }
    }

    val backCallbackOpenItem: BackCallback

    fun openTimeTableItem(item: AvailabilityItem) {
        backCallbackOpenItem.isEnabled = true

        (openedTimetableItem as MutableValue).value = true to item
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState)
}

class DefaultPresenceComponent(
    componentContext: ComponentContext, override val parent: MainComponent,
) : PresenceComponent, ComponentContext by componentContext {
    override val now: MutableValue<LocalDateTime> = MutableValue(Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")))
    override val currentPage = MutableValue(500 + now.value.date.dayOfWeek.ordinal)

    override val timetable: MutableValue<List<AvailabilityItem>> = MutableValue(emptyList())
    override val openedTimetableItem: MutableValue<Pair<Boolean, AvailabilityItem?>> = MutableValue(false to null)

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override val isRefreshingTimetable = MutableValue(false)

    override fun changeDay(day: Int) {
        currentPage.value = day

        if (selectedWeek.value != floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt())
            selectedWeek.value = floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt()
    }

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        runBlocking {
            isRefreshingTimetable.value = true
            try {
                println("Refreshing agenda for week $selectedWeek")

                timetable.value = readAvailability()
            } catch (e: MagisterException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRefreshingTimetable.value = false
        }
    }

    override val backCallbackOpenItem: BackCallback = BackCallback(false) {
        closeItemPopup()
    }

    private fun closeItemPopup() {
        openedTimetableItem.value = false to openedTimetableItem.value.second
        backCallbackOpenItem.isEnabled = false
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun scrollToPage(coroutineScope: CoroutineScope, page: Int, pagerState: PagerState) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
        currentPage.value = page
    }


    init {
        backHandler.register(backCallbackOpenItem)

        selectedWeek.observe {
            refreshSelectedWeek()
        }

        runBlocking {
            while (true) {
                now.value = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                delay(60_000)
            }
        }
    }

}

fun List<AvailabilityItem>.getAgendaForDay(day: Int): List<AvailabilityItem> {
    return this.filter { item ->
        item.startDateTime.dayOfWeek.ordinal == day
    }
}

val AvailabilityItem.startDateTime get() = Instant.fromEpochSeconds(startTime).toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
val AvailabilityItem.endDateTime get() = Instant.fromEpochSeconds(endTime).toLocalDateTime(TimeZone.of("Europe/Amsterdam"))