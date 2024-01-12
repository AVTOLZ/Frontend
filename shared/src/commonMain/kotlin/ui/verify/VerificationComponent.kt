package ui.verify

import Data
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ui.RootComponent

interface VerificationComponent {
    val parent: RootComponent
}

class DefaultVerificationComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : VerificationComponent, ComponentContext by componentContext {
}