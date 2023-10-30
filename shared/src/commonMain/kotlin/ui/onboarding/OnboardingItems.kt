package ui.onboarding

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.StringResource

internal class OnboardingItems(
    val title: String,
    val desc: String,
    val extraItems: @Composable () -> Unit = {}
) {
    companion object{
        fun getData(): List<OnboardingItems>{
            return listOf(
                OnboardingItems(
                    title = "ABCD",
                    desc = "abcd1"
                ),
                OnboardingItems(
                    title = "ABCD",
                    desc = "abcd1"
                ),
                OnboardingItems(
                    title = "ABCD",
                    desc = "abcd1"
                )
            )
        }
    }
}