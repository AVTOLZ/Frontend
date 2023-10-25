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


@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(component: RootComponent) {
    MaterialTheme {
        Children(
            stack = component.stack,
            modifier = Modifier,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.LoginChild -> LoginScreen(child.component)
                is RootComponent.Child.MainScreen -> MainScreen(child.component)

            }
        }
    }
}


expect fun getPlatformName(): String