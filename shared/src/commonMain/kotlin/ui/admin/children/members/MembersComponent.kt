package ui.admin.children.members

import api.admin.person.PersonData
import api.admin.person.getPeople
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ui.admin.AdminComponent
import ui.admin.AdminItemComponent

interface MembersComponent: AdminItemComponent {
    val members: Value<List<PersonData>>

}

class DefaultMembersComponent(
    componentContext: ComponentContext, override val parent: AdminComponent,
) : MembersComponent, ComponentContext by componentContext {
    override val members: MutableValue<List<PersonData>> = MutableValue(emptyList())

    init {
        GlobalScope.launch {
            members.value = getPeople() ?: return@launch
        }
    }

}