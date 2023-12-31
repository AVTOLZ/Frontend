package ui.main.children.presence

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.launch
import kotlinx.datetime.*

val timesShown = 8..20
val dpPerHour = 80.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun Timetable(
    component: PresenceComponent,
    dayPagerState: PagerState,
) {
    HorizontalPager(
        state = dayPagerState,
        beyondBoundsPageCount = 3
    ) { page ->
        val scope = rememberCoroutineScope()

        val now = remember { Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam")) }

        val startOfWeekDate = getStartOfWeekFromDay(page, dayPagerState.initialPage, now)
        val endOfWeekDate = startOfWeekDate.plus(6, DateTimeUnit.DAY)

        println(startOfWeekDate)

        var refreshing by remember { mutableStateOf(false) }

        val refreshState = rememberPullRefreshState(refreshing, {
            scope.launch {
                refreshing = true
                component.refreshTimetable(startOfWeekDate, endOfWeekDate)
                refreshing = false
            }
        })

        Box(
            Modifier
                .pullRefresh(refreshState)
                .verticalScroll(rememberScrollState())) {

            val minutes = ((now.hour - timesShown.first) * 60) + now.minute

            if (
                minutes > 0 &&
                now.hour in timesShown &&
                page == dayPagerState.pageCount / 2
            ) {
                Divider(
                    Modifier
                        .width(40.dp)
                        .padding(top = minutes / 60f * dpPerHour),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            TimetableGrid()

            TimetableItems(
                component = component,
                page = page,
                timesShown = timesShown,
                dpPerHour = dpPerHour,
            )


            PullRefreshIndicator(
                refreshing = refreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetablePreview() {
    Box(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Divider(
            Modifier
                .width(40.dp)
                .padding(top = 128 / 60f * dpPerHour),
            color = MaterialTheme.colorScheme.primary
        )

        TimetableGrid()
    }
}