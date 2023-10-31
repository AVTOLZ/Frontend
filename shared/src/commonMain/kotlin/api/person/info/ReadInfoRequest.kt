package api.person.info

import kotlinx.serialization.Serializable

@Serializable
data class ReadInfoRequest(val token: String)