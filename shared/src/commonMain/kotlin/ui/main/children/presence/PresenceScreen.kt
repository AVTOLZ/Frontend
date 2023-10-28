package ui.main.children.presence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresenceScreen(component: PresenceComponent) {
    val dayPagerState = rememberPagerState(component.currentPage.value)
    val weekPagerState = rememberPagerState(100)

    Column(modifier = Modifier.fillMaxSize()) {
        DaySelector(
            component = component,
            dayPagerState = dayPagerState,
            weekPagerState = weekPagerState
        )

        Timetable(
            component = component,
            dayPagerState = dayPagerState,
        )
    }

    val scope = rememberCoroutineScope()

    if (dayPagerState.currentPage != (component.amountOfDays / 2) + component.now.value.dayOfWeek.ordinal) {
        Box(Modifier.fillMaxSize()) {
            Button(
                onClick = { component.scrollToPage(scope, (component.amountOfDays / 2) + component.now.value.dayOfWeek.ordinal, dayPagerState) },
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

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val popupItem = component.openedTimetableItem.subscribeAsState()

        AnimatedVisibility(
            visible = popupItem.value.first,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            TimetableItemPopup(popupItem.value.second!!)
        }
    }

    var currentPage = remember { dayPagerState.currentPage }

    LaunchedEffect(dayPagerState.currentPage) {
        if (currentPage != dayPagerState.currentPage) {
            currentPage = dayPagerState.currentPage
            component.changeDay(dayPagerState.currentPage)
        }
    }

    component.selectedWeek.subscribe { week ->
        scope.launch {
            weekPagerState.animateScrollToPage(week + 100)
        }
    }
}