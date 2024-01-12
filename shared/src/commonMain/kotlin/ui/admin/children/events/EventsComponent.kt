package ui.admin.children.events

import api.admin.Event.Event
import api.admin.Event.getEvents
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ui.admin.AdminComponent
import ui.admin.AdminItemComponent

interface EventsComponent: AdminItemComponent {
    val events: Value<List<Event>>
}

class DefaultEventsComponent(
    componentContext: ComponentContext, override val parent: AdminComponent,
) : EventsComponent, ComponentContext by componentContext {
    override val events: MutableValue<List<Event>> = MutableValue(emptyList())

    init {
        GlobalScope.launch {
            events.value = getEvents() ?: emptyList<Event>().also {
                parent.parent.snackbarHost.showSnackbar("Error while getting events.")
            }

        }
    }


}