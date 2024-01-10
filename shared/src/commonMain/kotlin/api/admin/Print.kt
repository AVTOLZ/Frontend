package api.admin

import api.printUrl
import api.requests.postRequest
import kotlinx.serialization.Serializable


suspend fun printHours(hours: List<RequestedHour>) {
    val request = postRequest(printUrl.build(), PrintRequest(hours.map { it.hourId }))
}

@Serializable
data class PrintRequest(
    val requests: List<Int>
)
