package api.person.absence.requestHours

import api.client
import api.requestHoursUrl
import api.requests.postRequest
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun requestHours(hourId: Int, remove: Boolean): Boolean? {
    val request = postRequest(requestHoursUrl.build(), RequestHoursRequest(hourId, remove)) ?: return null

    return request.status == HttpStatusCode.OK
}