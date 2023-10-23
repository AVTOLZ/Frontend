import androidx.compose.runtime.Composable
import ui.DefaultRootComponent

actual fun getPlatformName(): String = "Android"

@Composable fun MainView(root: DefaultRootComponent) = App(root)
