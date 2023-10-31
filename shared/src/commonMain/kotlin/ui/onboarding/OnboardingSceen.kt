package ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ui.RootComponent
import ui.onboarding.sections.BottomSection
import ui.onboarding.sections.TopSection


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OnboardingScreen(parent: RootComponent) {
    val items = OnboardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = { items.size })

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(
            onBackClick = {
                if (pageState.currentPage + 1 > 1) scope.launch {
                    pageState.scrollToPage(pageState.currentPage - 1)
                }
            },
            onSkipClick = {
                if (pageState.currentPage + 1 < items.size) scope.launch {
                    pageState.scrollToPage(items.size - 1)
                }
            }
        )

        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            OnboardingMenuItem(items = items[page])
        }
        BottomSection(size = items.size, index = pageState.currentPage) {
            if (pageState.currentPage + 1 < items.size) {
                scope.launch {
                    pageState.scrollToPage(pageState.currentPage + 1)
                }
            } else {
                scope.launch {
                    Data.onboardingCompleted = true

                    parent.clearStack(parent.getInitialConfiguration())
                }
            }
        }
    }
}