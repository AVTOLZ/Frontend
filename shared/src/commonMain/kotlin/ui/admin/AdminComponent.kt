package ui.admin

import androidx.compose.runtime.Composable
import api.person.info.readInfo
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import ui.RootComponent
import ui.admin.children.members.DefaultMembersComponent
import ui.admin.children.members.MembersComponent
import ui.main.MainComponent
import ui.main.MenuItemComponent
import ui.main.children.presence.DefaultPresenceComponent

interface AdminItemComponent {
    val parent: AdminComponent
}

interface AdminComponent {
    val parent: RootComponent

    fun navigateTo(config: Config)

    @Serializable // kotlinx-serialization plugin must be applied
    sealed class Config(val text: String, val icon: @Composable () -> Unit) {
        @Serializable
        data object Members : Config("Members", {

        })
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
        }
    }

    private fun membersComponent(componentContext: ComponentContext): MembersComponent =
        DefaultMembersComponent(componentContext, this)

    override fun navigateTo(config: AdminComponent.Config) {
        dialogNavigation.activate(config)
    }

    init {
        runBlocking {
            readInfo()
        }
    }
}