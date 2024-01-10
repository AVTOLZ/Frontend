package ui.login

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

interface MagisterLoginComponent {
    val parent: RootComponent


}

class DefaultMagisterLoginComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : MagisterLoginComponent, ComponentContext by componentContext {

}