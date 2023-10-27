package api.person.absence.availability

import kotlinx.serialization.Serializable

@Serializable
data class ReadAvailabilityRequest(val token: String)