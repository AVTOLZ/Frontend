package api.admin.person

import api.person.info.AVTRanks
import kotlinx.serialization.Serializable

@Serializable
data class PersonData (
    val id: Int,
    val username: String,
    val password: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val studentId: Int?,
    val rank: AVTRanks,
    val state: UserState
)

enum class UserState {
    UNVERIFIED, VERIFIED, DISABLED
}