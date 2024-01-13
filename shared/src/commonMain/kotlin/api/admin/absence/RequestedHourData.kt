package api.admin.absence

import api.admin.event.Event
import api.admin.person.PersonData
import api.person.absence.availability.PresenceType
import kotlinx.serialization.Serializable

@Serializable
data class RequestedHourData(
    val user: PersonData,
    val hour: Event,
    val approver: PersonData?,
    val timeApproved: Long?,
    val presenceType: PresenceType,
    val state: State,
    val timeRequested: Long
)

@Serializable
data class HourPatchRequest(val id: Int, val approved: Boolean)

enum class State {
    NONE,
    APPROVED,
    PROCESSED
}