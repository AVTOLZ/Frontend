package ui.login

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

interface LoginComponent {
    val parent: RootComponent


}

class DefaultLoginComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : LoginComponent, ComponentContext by componentContext {

}