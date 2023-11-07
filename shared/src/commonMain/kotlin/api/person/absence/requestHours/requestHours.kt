package api.person.absence.requestHours

import api.client
import api.requestHoursUrl
import api.requests.postRequest
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun requestHours(hourId: Int, requestType: HourRequestType): Boolean? {
    val request = postRequest(requestHoursUrl.build(), RequestHoursRequest(hourId, requestType)) ?: return null

    return request.status == HttpStatusCode.OK
}