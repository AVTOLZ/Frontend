package ui.admin

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ui.RootComponent
import ui.admin.children.events.DefaultEventsComponent
import ui.admin.children.events.EventsComponent
import ui.admin.children.hours.DefaultHoursComponent
import ui.admin.children.hours.HoursComponent
import ui.admin.children.members.DefaultMembersComponent
import ui.admin.children.members.MembersComponent

interface AdminItemComponent {
    val parent: AdminComponent
}

interface AdminComponent {
    val parent: RootComponent

    fun navigateTo(config: Config)

    @Serializable // kotlinx-serialization plugin must be applied
    sealed class Config(val text: String) {
        @Serializable
        data object Members : Config("Members")

        @Serializable
        data object Events : Config("Events")

        @Serializable
        data object Hours : Config("Hours")
    }


    val dialog: Value<ChildSlot<Config, AdminItemComponent>>
}

class DefaultAdminComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
): AdminComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<AdminComponent.Config>()

    override val dialog: Value<ChildSlot<AdminComponent.Config, AdminItemComponent>> = childSlot(
        source = dialogNavigation,
        serializer = AdminComponent.Config.serializer(),
        initialConfiguration = { AdminComponent.Config.Members },
        handleBackButton = false
    ) { config: AdminComponent.Config, componentContext: ComponentContext ->
        when (config) {
            is AdminComponent.Config.Members -> membersComponent(componentContext)
            is AdminComponent.Config.Events -> eventsComponent(componentContext)
            is AdminComponent.Config.Hours -> hoursComponent(componentContext)
        }
    }

    private fun membersComponent(componentContext: ComponentContext): MembersComponent =
        DefaultMembersComponent(componentContext, this)

    private fun eventsComponent(componentContext: ComponentContext): EventsComponent =
        DefaultEventsComponent(componentContext, this)

    private fun hoursComponent(componentContext: ComponentContext): HoursComponent =
        DefaultHoursComponent(componentContext, this)


    override fun navigateTo(config: AdminComponent.Config) {
        dialogNavigation.activate(config)
    }
}