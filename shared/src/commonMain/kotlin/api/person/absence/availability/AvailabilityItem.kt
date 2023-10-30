package api.person.absence.availability

import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityItem(val id: Int, val startTime: Long, val endTime: Long, val status: HourStatus)