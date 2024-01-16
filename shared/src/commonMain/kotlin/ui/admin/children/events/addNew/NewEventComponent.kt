package ui.admin.children.events.addNew

import com.arkivanov.decompose.ComponentContext
import ui.admin.AdminComponent
import ui.admin.AdminItemComponent

interface NewEventComponent : AdminItemComponent{

}

class DefaultNewEventComponent(
    componentContext: ComponentContext, override val parent: AdminComponent,
) : NewEventComponent, ComponentContext by componentContext {
}