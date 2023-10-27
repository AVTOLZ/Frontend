package ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ui.RootComponent
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceComponent
import ui.main.icons.CalendarTodayIcon

interface MenuItemComponent {
    val parent: MainComponent
}

interface MainComponent {
    val parent: RootComponent

    fun onBackClicked(toIndex: Int)

    fun navigateTo(config: Config)

    @Serializable // kotlinx-serialization plugin must be applied
    sealed class Config(val text: String, val icon: @Composable () -> Unit) {
        @Serializable
        data object Presence : Config("Presence", {
            Icon(CalendarTodayIcon, "Presence")
        })
    }


    val dialog: Value<ChildSlot<Config, MenuItemComponent>>
}

class DefaultMainComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : MainComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<MainComponent.Config>()

    private val dialogNavigation = SlotNavigation<MainComponent.Config>()

    override val dialog: Value<ChildSlot<MainComponent.Config, MenuItemComponent>> = childSlot<MainComponent.Config, MenuItemComponent>(
        source = dialogNavigation,
        serializer = MainComponent.Config.serializer(),
        initialConfiguration = { MainComponent.Config.Presence },
        handleBackButton = false
    ) { config: MainComponent.Config, componentContext: ComponentContext ->
        when (config) {
            MainComponent.Config.Presence -> presenceComponent(componentContext)
        }
    }

    private fun presenceComponent(componentContext: ComponentContext): PresenceComponent =
        DefaultPresenceComponent(componentContext, this)

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    override fun navigateTo(config: MainComponent.Config) {
        navigation.push(config)
    }
}