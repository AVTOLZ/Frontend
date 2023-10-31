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
                    title = "Welcome to the AVT app",
                    desc = "By Abel, Tiebe and Koen."
                ),
                OnboardingItems(
                    title = "What is this app?",
                    desc = "This app is designed to assist with managing the AVT. \n" +
                            "you can easily get an overview of upcoming event and request to be present."
                ),
                OnboardingItems(
                    title = "Logging in",
                    desc = "There are two methods you can use to log in: \n" +
                            "1: with a username, password and email. this gives access to the basic features. \n" +
                            "2: via magister. by logging in with magister, our systems can automatically check whether " +
                            "you are allowed to help in accordance to school rules. this gives access to all features."
                )
            )
        }
    }
}