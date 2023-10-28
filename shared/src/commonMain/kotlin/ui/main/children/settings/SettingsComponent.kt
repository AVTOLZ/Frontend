package ui.main.children.settings

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

// TODO finish implementing this

interface SettingsComponent {
    val parent: RootComponent
}

class DefaultSettingsComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : SettingsComponent, ComponentContext by componentContext {

}