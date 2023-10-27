package api.person.info

import kotlinx.serialization.Serializable

@Serializable
data class ReadInfoResponse(val username: String, val firstName: String, val lastName: String, val rank: AVTRanks)