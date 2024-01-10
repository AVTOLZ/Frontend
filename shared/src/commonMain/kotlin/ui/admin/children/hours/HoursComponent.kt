package ui.admin.children.hours

import api.admin.RequestedHour
import api.admin.getRequestedHours
import api.admin.printHours
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ui.admin.AdminComponent
import ui.admin.AdminItemComponent

interface HoursComponent: AdminItemComponent {
    val hours: Value<List<RequestedHour>>

    suspend fun print(hours: List<RequestedHour>)
}

class DefaultHoursComponent(
    componentContext: ComponentContext, override val parent: AdminComponent,
) : HoursComponent, ComponentContext by componentContext {
    override val hours: MutableValue<List<RequestedHour>> = MutableValue(emptyList())
    override suspend fun print(hours: List<RequestedHour>) {
        printHours(hours)
    }

    init {
        GlobalScope.launch {
            hours.value = getRequestedHours() ?: emptyList<RequestedHour>().also {
                parent.parent.snackbarHost.showSnackbar("Error while getting requested hours.")
            }

        }
    }


}