package ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import ui.login.LoginComponent
import kotlinx.serialization.Serializable
import ui.login.DefaultLoginComponent
import ui.main.DefaultMainComponent
import ui.main.MainComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    fun navigateTo(config: Config)

    sealed class Child {
        class LoginChild(val component: LoginComponent) : Child()

        class MainScreen(val component: MainComponent) : Child()
    }

    @Serializable // kotlinx-serialization plugin must be applied
    sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Main : Config
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<RootComponent.Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootComponent.Config.serializer(),
            initialConfiguration = getInitialConfiguration(), // The initial child component is List
            handleBackButton = true, // Automatically pop from the stack on back button presses
            childFactory = ::child,
        )

    private fun getInitialConfiguration(): RootComponent.Config {
        if (Data.bearerToken == null) {
            return RootComponent.Config.Login
        }

        return RootComponent.Config.Main
    }

    private fun child(config: RootComponent.Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is RootComponent.Config.Login -> RootComponent.Child.LoginChild(loginComponent(componentContext))
            is RootComponent.Config.Main -> RootComponent.Child.MainScreen(mainComponent(componentContext))
        }

    private fun loginComponent(componentContext: ComponentContext): LoginComponent =
        DefaultLoginComponent(componentContext, this)

    private fun mainComponent(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(componentContext, this)

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    override fun navigateTo(child: RootComponent.Config) {
        navigation.push(child)
    }
}