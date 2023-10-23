package ui.main

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

interface MainComponent {
    val parent: RootComponent


}

class DefaultMainComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : MainComponent, ComponentContext by componentContext {

}