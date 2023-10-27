package ui.presence

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

interface PresenceComponent {
    val parent: RootComponent
}

class DefaultPresenceComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : PresenceComponent, ComponentContext by componentContext {

}