package ui.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ui.RootComponent
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceComponent

interface MainComponent {
    val parent: RootComponent
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    fun navigateTo(config: Config)

    sealed class Child {
        class PresenceScreen(val component: PresenceComponent) : Child()
    }

    @Serializable // kotlinx-serialization plugin must be applied
    sealed interface Config {
        @Serializable
        data object Presence : Config
    }

}

class DefaultMainComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : MainComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<MainComponent.Config>()

    override val stack: Value<ChildStack<*, MainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = MainComponent.Config.serializer(),
            initialConfiguration = getInitialConfiguration(), // The initial child component is List
            handleBackButton = true, // Automatically pop from the stack on back button presses
            childFactory = ::child,
        )

    private fun getInitialConfiguration(): MainComponent.Config {
        return MainComponent.Config.Presence
    }

    private fun child(config: MainComponent.Config, componentContext: ComponentContext): MainComponent.Child =
        when (config) {
            MainComponent.Config.Presence -> MainComponent.Child.PresenceScreen(presenceComponent(componentContext))
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