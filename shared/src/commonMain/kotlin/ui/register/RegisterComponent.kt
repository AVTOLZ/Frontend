package ui.register

import androidx.compose.material3.SnackbarHostState
import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

interface RegisterComponent {
    val parent: RootComponent
}

class DefaultRegisterComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : RegisterComponent, ComponentContext by componentContext {
}