package ui.onboarding

import androidx.compose.runtime.Composable

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
                            "You can easily get an overview of upcoming event and request to be present."
                ),
                OnboardingItems(
                    title = "Logging in",
                    desc = "There are two methods you can use to log in: \n" +
                            "1: With a username and password. This gives access to the basic features. \n" +
                            "2: Via magister. By logging in with magister, our systems can automatically check whether " +
                            "you are allowed to help in accordance to school rules. This gives access to all features."
                )
            )
        }
    }
}
