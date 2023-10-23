import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.DefaultRootComponent
import ui.RootComponent
import ui.login.LoginScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(component: DefaultRootComponent) {
    MaterialTheme {
        Children(
            stack = component.stack,
            modifier = Modifier,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.LoginChild -> LoginScreen(child.component)
            }
        }
    }
    }
}

expect fun getPlatformName(): String