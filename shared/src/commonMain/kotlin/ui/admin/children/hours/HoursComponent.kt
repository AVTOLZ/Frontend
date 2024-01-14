package ui.admin.children.hours

import api.admin.absence.RequestedHourData
import api.admin.absence.getRequestedHours
import api.admin.printHours
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ui.admin.AdminComponent
import ui.admin.AdminItemComponent

interface HoursComponent: AdminItemComponent {
    val hours: Value<List<RequestedHourData>>

    suspend fun print(hours: List<RequestedHourData>)
}

class DefaultHoursComponent(
    componentContext: ComponentContext, override val parent: AdminComponent,
) : HoursComponent, ComponentContext by componentContext {
    override val hours: MutableValue<List<RequestedHourData>> = MutableValue(emptyList())
    override suspend fun print(hours: List<RequestedHourData>) {
        printHours(hours)
    }

    init {
        GlobalScope.launch {
            hours.value = getRequestedHours() ?: emptyList<RequestedHourData>().also {
                parent.parent.snackbarHost.showSnackbar("Error while getting requested hours.")
            }

        }
    }


}