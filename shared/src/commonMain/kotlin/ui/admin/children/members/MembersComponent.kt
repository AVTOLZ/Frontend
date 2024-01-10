package ui.admin.children.members

import com.arkivanov.decompose.ComponentContext
import ui.admin.AdminComponent
import ui.admin.AdminItemComponent

interface MembersComponent: AdminItemComponent {

}

class DefaultMembersComponent(
    componentContext: ComponentContext, override val parent: AdminComponent,
) : MembersComponent, ComponentContext by componentContext {

}