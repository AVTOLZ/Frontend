package api.accounts

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val personId: Int, val token: String, val verified: Boolean)