package ui.main.children.settings

import api.person.info.readInfo
import com.arkivanov.decompose.ComponentContext
import ui.main.MainComponent
import ui.main.MenuItemComponent

// TODO finish implementing this

interface SettingsComponent: MenuItemComponent {

}

class DefaultSettingsComponent(
    componentContext: ComponentContext, override val parent: MainComponent,
) : SettingsComponent, ComponentContext by componentContext