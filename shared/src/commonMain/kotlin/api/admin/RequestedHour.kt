package api.admin

import api.person.absence.availability.PresenceType
import kotlinx.serialization.Serializable

@Serializable
data class RequestedHour(
    val userId: Int,
    val hourId: Int,
    val approver: Int?,
    val timeApproved: Long?,
    val presenceType: PresenceType,
    val state: State,
    val timeRequested: Long
)

enum class State {
    NONE,
    APPROVED,
    PROCESSED
}