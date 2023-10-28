package ui.main.children.presence

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TimetableScreen(component: PresenceComponent) {
    var currentTime = remember {
        Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    }

    val dayPagerState = rememberPagerState(350 + currentTime.dayOfWeek.ordinal) { 700 }
    val weekPagerState = rememberPagerState(50) { 100 }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
            delay(10000)
        }
    }

    LaunchedEffect(dayPagerState.currentPage) {
        val startOfWeekDate = getStartOfWeekFromDay(dayPagerState.currentPage, 350)
        component.refreshTimetable(startOfWeekDate)

        weekPagerState.animateScrollToPage(dayPagerState.currentPage / 7)
    }



    Column(modifier = Modifier.fillMaxSize()) {
        DaySelector(
            dayPagerState = dayPagerState,
            weekPagerState = weekPagerState
        )

        Timetable(
            component = component,
            dayPagerState = dayPagerState,
        )
    }

    TodayButton(dayPagerState, currentTime.dayOfWeek.ordinal)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayButton(dayPagerState: PagerState, currentDayOfWeek: Int) {
    val scope = rememberCoroutineScope()

    val todayIndex = remember {
        dayPagerState.pageCount / 2 + currentDayOfWeek
    }

    if (dayPagerState.currentPage != todayIndex) {
        Box(Modifier.fillMaxSize()) {
            Button(
                onClick = { scope.launch { dayPagerState.scrollToPage(todayIndex) } },
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Today",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun PresenceScreen(component: PresenceComponent) {
//    val dayPagerState = rememberPagerState(component.currentPage.value)
//    val weekPagerState = rememberPagerState(100)
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        DaySelector(
//            component = component,
//            dayPagerState = dayPagerState,
//            weekPagerState = weekPagerState
//        )
//
//        Timetable(
//            component = component,
//            dayPagerState = dayPagerState,
//        )
//    }
//
//    val scope = rememberCoroutineScope()
//
//    if (dayPagerState.currentPage != (component.amountOfDays / 2) + component.now.value.dayOfWeek.ordinal) {
//        Box(Modifier.fillMaxSize()) {
//            Button(
//                onClick = { component.scrollToPage(scope, (component.amountOfDays / 2) + component.now.value.dayOfWeek.ordinal, dayPagerState) },
//                modifier = Modifier
//                    .size(60.dp)
//                    .padding(10.dp)
//                    .align(Alignment.BottomEnd),
//                shape = CircleShape,
//                contentPadding = PaddingValues(0.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Home,
//                    contentDescription = "Today",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//        }
//    }
//
//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        val popupItem = component.openedTimetableItem.subscribeAsState()
//
//        AnimatedVisibility(
//            visible = popupItem.value.first,
//            enter = fadeIn(),
//            exit = fadeOut(),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            TimetableItemPopup(popupItem.value.second!!)
//        }
//    }
//
//    var currentPage = remember { dayPagerState.currentPage }
//
//    LaunchedEffect(dayPagerState.currentPage) {
//        if (currentPage != dayPagerState.currentPage) {
//            currentPage = dayPagerState.currentPage
//            component.changeDay(dayPagerState.currentPage)
//        }
//    }
//
//    component.selectedWeek.subscribe { week ->
//        scope.launch {
//            weekPagerState.animateScrollToPage(week + 100)
//        }
//    }
//}