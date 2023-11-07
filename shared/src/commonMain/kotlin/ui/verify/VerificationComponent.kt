package ui.verify

import api.accounts.verifyAccount
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.runBlocking
import ui.RootComponent

interface VerificationComponent {
    val parent: RootComponent

    fun verify(code: String)

}

class DefaultVerificationComponent(
    componentContext: ComponentContext, override val parent: RootComponent,
) : VerificationComponent, ComponentContext by componentContext {

    override fun verify(code: String) {
        // TODO make this async as well with loading indicator

        val success = runBlocking {
            return@runBlocking verifyAccount(Data.personId, code.toIntOrNull() ?: return@runBlocking false)
        }

        if (success != true){
            runBlocking {
            parent.snackbarHost.showSnackbar("There was an error validating your request") }
        }

        if (success == true) {
            parent.clearStack(RootComponent.Config.Main)
        }
    }

}