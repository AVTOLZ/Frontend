package ui.verify

import com.arkivanov.decompose.ComponentContext
import ui.RootComponent

interface VerificationComponent {
    val parent: RootComponent

    fun verify(code: String)

}

class DefaultVerificationComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : VerificationComponent, ComponentContext by componentContext {

    override fun verify(code: String) {

    }

}