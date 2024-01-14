package api.admin.event

import api.person.info.AVTRanks
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int? = null,
    val requiredRank: AVTRanks,
    var startTime: Long,
    var endTime: Long,
    var title: String,
    var description: String
)