
package ui

import Data
import androidx.compose.material3.SnackbarHostState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import ui.login.LoginComponent
import kotlinx.serialization.Serializable
import ui.login.DefaultLoginComponent
import ui.main.DefaultMainComponent
import ui.main.MainComponent
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceComponent
import ui.register.DefaultRegisterComponent
import ui.register.RegisterComponent
import ui.verify.DefaultVerificationComponent
import ui.verify.VerificationComponent
import kotlin.coroutines.CoroutineContext

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    fun navigateTo(config: Config)

    fun clearStack(newConfig: Config)

    val snackbarHost: SnackbarHostState

    sealed class Child {
        class LoginChild(val component: LoginComponent) : Child()

        class MainScreen(val component: MainComponent) : Child()

        class Verify(val component: VerificationComponent) : Child()

        class Register(val component: RegisterComponent) : Child()

        data object Onboarding : Child()
    }

    @Serializable // kotlinx-serialization plugin must be applied
    sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Main : Config

        @Serializable
        data object Verify : Config

        @Serializable
        data object Register : Config

        @Serializable
        data object Onboarding : Config
    }

    fun getInitialConfiguration(): Config
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

    override fun getInitialConfiguration(): RootComponent.Config {
        if (!Data.onboardingCompleted) {
            return RootComponent.Config.Onboarding
        }

        if (Data.bearerToken == null) {
            return RootComponent.Config.Login
        }

        if (!Data.verified) return RootComponent.Config.Verify

        return RootComponent.Config.Main
    }

    private fun child(config: RootComponent.Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is RootComponent.Config.Login -> RootComponent.Child.LoginChild(loginComponent(componentContext))
            is RootComponent.Config.Main -> RootComponent.Child.MainScreen(mainComponent(componentContext))
            is RootComponent.Config.Verify -> RootComponent.Child.Verify(verificationComponent(componentContext))
            is RootComponent.Config.Register -> RootComponent.Child.Register(registerComponent(componentContext))
            is RootComponent.Config.Onboarding -> RootComponent.Child.Onboarding
        }

    private fun loginComponent(componentContext: ComponentContext): LoginComponent =
        DefaultLoginComponent(componentContext, this)

    private fun mainComponent(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(componentContext, this)

    private fun verificationComponent(componentContext: ComponentContext): VerificationComponent =
        DefaultVerificationComponent(componentContext, this)

    private fun registerComponent(componentContext: ComponentContext): RegisterComponent =
        DefaultRegisterComponent(componentContext, this)

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    override fun navigateTo(config: RootComponent.Config) {
        navigation.push(config)
    }

    override fun clearStack(newConfig: RootComponent.Config) {
        navigation.replaceAll(newConfig)
    }

    override val snackbarHost = SnackbarHostState()
}

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.componentCoroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

fun LifecycleOwner.coroutineScope(context: CoroutineContext) = componentCoroutineScope(context)