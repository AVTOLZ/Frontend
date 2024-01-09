
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.DelicateCoroutinesApi
import org.jetbrains.skiko.wasm.onWasmReady
import ui.DefaultRootComponent

@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Timesheet", "ComposeTarget") {
            val lifecycle = LifecycleRegistry()

            val root =
                DefaultRootComponent(
                    componentContext = DefaultComponentContext(lifecycle = lifecycle),
            )

            // Render the UI
            App(root)
        }
    }
}
