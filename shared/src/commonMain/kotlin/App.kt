import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import io.ktor.client.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.DefaultRootComponent
import ui.RootComponent
import ui.login.LoginScreen
import ui.main.MainScreen
import ui.onboarding.OnboardingScreen
import ui.register.RegisterScreen
import ui.verify.VerificationScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(component: RootComponent) {
    MaterialTheme {
        Children(
            stack = component.stack,
            modifier = Modifier.fillMaxSize(),
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.LoginChild -> LoginScreen(child.component)
                is RootComponent.Child.MainScreen -> MainScreen(child.component)
                is RootComponent.Child.Verify -> VerificationScreen(child.component)
                is RootComponent.Child.Register -> RegisterScreen(child.component)
                is RootComponent.Child.Onboarding -> OnboardingScreen(component)
            }
        }
    }
}