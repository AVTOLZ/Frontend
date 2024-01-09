package ui.main.children.presence

import Platform
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import currentPlatform
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.math.floor

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DaySelector(
    dayPagerState: PagerState,
    weekPagerState: PagerState,
) {
    val scope = rememberCoroutineScope()

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (currentPlatform == Platform.JVM) {
            Button(
                onClick = { scope.launch { dayPagerState.scrollToPage(dayPagerState.currentPage - 7) } },
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp),
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

        HorizontalPager(state = weekPagerState) { week ->
            TabRow(
                selectedTabIndex = (dayPagerState.currentPage - dayPagerState.pageCount) % days.size,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(
                            dayPagerState,
                            tabPositions,
                            shouldShowIndicator = week == floor(dayPagerState.currentPage / days.size.toDouble()).toInt()
                        )
                    )
                }) {

                days.forEachIndexed { dayIndex, title ->
                    val index = dayIndex + (week * days.size)
                    DayTabItem(
                        index == dayPagerState.currentPage &&
                                week == floor(dayPagerState.currentPage / days.size.toDouble()).toInt(),
                        index,
                        dayPagerState,
                        title
                    )
                }

                if (currentPlatform == Platform.JVM) {
                    // TODO fix this
                    Button(
                        onClick = { scope.launch { dayPagerState.scrollToPage(dayPagerState.currentPage + 7) } },
                        modifier = Modifier
                            .size(60.dp)
                            .padding(10.dp),
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayTabItem(
    selected: Boolean,
    dayIndex: Int,
    dayPagerState: PagerState,
    title: String
) {
    val scope = rememberCoroutineScope()
    val currentDate = remember {
        Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    }

    val firstDayOfWeek = remember {
        currentDate.date.minus(currentDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
    }

    Tab(
        selected = selected,
        onClick = {
            scope.launch {
                dayPagerState.animateScrollToPage(dayIndex)
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp
                )
                Text(
                    text = firstDayOfWeek.plus(dayIndex - dayPagerState.pageCount / 2, DateTimeUnit.DAY).toString()
                        .split("-").reversed().subList(0, 1).joinToString(),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp
                )
            }

        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySelectorPreview() {
    val currentDate = remember {
        Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))
    }

    DaySelector(
        dayPagerState = rememberPagerState(350 + currentDate.dayOfWeek.ordinal) { 700 },
        weekPagerState = rememberPagerState(50) { 100 }
    )
}


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.tabIndicatorOffset(
    dayPagerState: PagerState,
    tabPositions: List<TabPosition>,
    shouldShowIndicator: Boolean = true,
    selectedTabIndex: Int = getPage(dayPagerState, dayPagerState.pageCount)
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPositions[selectedTabIndex]
    }
) {
    val currentPage = minOf(tabPositions.lastIndex, selectedTabIndex)

    val currentTabPosition = tabPositions[currentPage]
    val nextTabPosition = tabPositions.getOrNull(currentPage + 1)
    val previousTabPosition = tabPositions.getOrNull(currentPage - 1)

    val fraction = dayPagerState.currentPageOffsetFraction

    var indicatorWidth: Dp
    val indicatorOffset: Dp

    if (fraction > 0 && nextTabPosition != null) {
        indicatorWidth = lerp(currentTabPosition.width, nextTabPosition.width, fraction)
        indicatorOffset = lerp(currentTabPosition.left, nextTabPosition.left, fraction)
    } else if (fraction < 0 && previousTabPosition != null) {
        indicatorWidth = lerp(currentTabPosition.width, previousTabPosition.width, -fraction)
        indicatorOffset = lerp(currentTabPosition.left, previousTabPosition.left, -fraction)
    } else {
        indicatorWidth = currentTabPosition.width
        indicatorOffset = currentTabPosition.left
    }

    if (!shouldShowIndicator) {
        indicatorWidth = 0.dp
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(indicatorWidth)
}


//todo FIX THIS ONE DAY
@OptIn(ExperimentalFoundationApi::class)
fun getPage(pagerState: PagerState, pageCount: Int): Int {
    return if ((pagerState.currentPage - (pageCount / 2)) % 7 >= 0) {
        (pagerState.currentPage - (pageCount / 2)) % 7
    } else {
        (pagerState.currentPage - (pageCount / 2)) % 7 + 7
    }
}