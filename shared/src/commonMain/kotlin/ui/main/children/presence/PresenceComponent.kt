package ui.main.children.presence

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import ui.login.LoginComponent
import kotlinx.serialization.Serializable
import ui.login.DefaultLoginComponent
import ui.main.DefaultMainComponent
import ui.main.MainComponent
import ui.main.MenuItemComponent
import ui.main.children.presence.DefaultPresenceComponent
import ui.main.children.presence.PresenceComponent
import ui.register.DefaultRegisterComponent
import ui.register.RegisterComponent
import ui.verify.DefaultVerificationComponent
import ui.verify.VerificationComponent

interface PresenceComponent: MenuItemComponent {

}

class DefaultPresenceComponent(
    componentContext: ComponentContext, override val parent: MainComponent,
) : PresenceComponent, ComponentContext by componentContext {
}