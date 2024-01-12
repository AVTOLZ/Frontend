package api.admin

import api.admin.absence.RequestedHourData
import api.printUrl
import api.requests.postRequest
import kotlinx.serialization.Serializable


suspend fun printHours(hours: List<RequestedHourData>) {
    val request = postRequest(printUrl.build(), PrintRequest(hours.map {
        it.hour.id ?: throw NullPointerException()
    }))
}

@Serializable
data class PrintRequest(
    val requests: List<Int>
)
