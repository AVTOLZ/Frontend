package api.person.absence.requestHours

import kotlinx.serialization.Serializable

@Serializable
data class RequestHoursRequest(val hour: Int)