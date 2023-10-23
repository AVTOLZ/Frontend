package ui.login

import com.arkivanov.decompose.ComponentContext

interface LoginComponent {

}

class DefaultLoginComponent(
    componentContext: ComponentContext,
) : LoginComponent, ComponentContext by componentContext {

}