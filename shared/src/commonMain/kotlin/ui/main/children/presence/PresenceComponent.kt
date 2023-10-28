package ui.main.children.presence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.arkivanov.decompose.ComponentContext
import ui.RootComponent
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.tiebe.magisterapi.api.absence.AbsenceFlow.getAbsences
import dev.tiebe.magisterapi.utils.MagisterException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import ui.login.LoginComponent
import kotlinx.serialization.Serializable
import ui.componentCoroutineScope
import ui.login.DefaultLoginComponent
import ui.main.DefaultMainComponent
import ui.main.MainComponent
import ui.main.MenuItemComponent
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceComponent
import ui.register.DefaultRegisterComponent
import ui.register.RegisterComponent
import ui.verify.DefaultVerificationComponent
import ui.verify.VerificationComponent
import kotlin.math.floor

interface PresenceComponent: MenuItemComponent {
    val now: Value<LocalDateTime>
    val firstDayOfWeek get() = now.value.date.minus(now.value.date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    val amountOfDays get() = 1000

    val currentPage: Value<Int>

    val days: List<String>

    val timetable: Value<List<AgendaItemWithAbsence>>
    val account: MagisterAccount

    val openedTimetableItem: Value<Pair<Boolean, AgendaItemWithAbsence?>>

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

    fun getTimetableForWeek(timetable: List<AgendaItemWithAbsence>, startOfWeekDate: LocalDate): List<AgendaItemWithAbsence> {
        return timetable.filter {
            val startTime =
                it.agendaItem.start.substring(0, 26).toLocalDateTime()

            startTime.date in startOfWeekDate..startOfWeekDate.plus(
                6,
                DateTimeUnit.DAY
            )
        }
    }

    val backCallbackOpenItem: BackCallback

    fun openTimeTableItem(item: AgendaItemWithAbsence) {
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

    override val days = listOf(
        "mon",
        "tue",
        "wed",
        "thu",
        "fri",
        "sat",
        "sun"
    )

    override val timetable: MutableValue<List<AgendaItemWithAbsence>> = MutableValue(emptyList())
    override val account: MagisterAccount = Data.selectedAccount
    override val openedTimetableItem: MutableValue<Pair<Boolean, AgendaItemWithAbsence?>> = MutableValue(false to null)

    override val selectedWeek = MutableValue(floor((currentPage.value - (amountOfDays / 2).toFloat()) / days.size).toInt())

    override val isRefreshingTimetable = MutableValue(false)

    override fun changeDay(day: Int) {
        currentPage.value = day

        if (selectedWeek.value != floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt())
            selectedWeek.value = floor((day - (amountOfDays / 2).toFloat()) / days.size).toInt()
    }

    private val scope = componentCoroutineScope(SupervisorJob())

    override fun refreshTimetable(from: LocalDate, to: LocalDate) {
        scope.launch {
            isRefreshingTimetable.value = true
            try {
                println("Refreshing agenda for week $selectedWeek")

                val timeTableWeek = getAbsences(
                    account.accountId,
                    account.tenantUrl,
                    account.tokens.accessToken,
                    "${from.year}-${from.month}-${from.dayOfMonth}",
                    "${to.year}-${to.month}-${to.dayOfMonth}",
                    getMagisterAgenda(
                        account.accountId,
                        account.tenantUrl,
                        account.tokens.accessToken,
                        from,
                        to
                    )
                )

                if (selectedWeek.value == 0) {
                    println("Saving agenda for current week")
                    account.agenda = timeTableWeek
                }

                timeTableWeek.forEach {
                    if (timetable.value.find { item -> item.agendaItem.id == it.agendaItem.id } == null) {
                        timetable.value = timetable.value + it
                    } else {
                        timetable.value = timetable.value.map { item ->
                            if (item.agendaItem.id == it.agendaItem.id) {
                                it
                            } else {
                                item
                            }
                        }
                    }
                }
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

        scope.launch {
            while (true) {
                now.value = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))

                delay(60_000)
            }
        }
    }

}