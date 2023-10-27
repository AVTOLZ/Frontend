package dev.avt.api.person.absence.availability

import api.person.absence.availability.HourStatus
import kotlinx.serialization.Serializable

@Serializable
data class HourDataFormat(val id: Int, val startTime: Long, val endTime: Long, val status: HourStatus)