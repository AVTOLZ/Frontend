package api.person.absence.requestHours

import api.requestHoursUrl
import api.requests.deleteRequest
import api.requests.postRequest
import io.ktor.http.*

suspend fun requestHours(hourId: Int, remove: Boolean): HttpStatusCode? {
    val request = if (remove) {
        deleteRequest(requestHoursUrl.build(), RequestHoursRequest(hourId)) ?: return null
    } else {

        postRequest(requestHoursUrl.build(), RequestHoursRequest(hourId)) ?: return null
    }

    return request.status
}