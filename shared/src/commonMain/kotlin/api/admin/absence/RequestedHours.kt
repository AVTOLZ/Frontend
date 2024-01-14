package api.admin.absence

import api.hoursUrl
import api.requests.getRequest
import api.requests.patchRequest
import io.ktor.client.call.*
import io.ktor.http.*

suspend fun getRequestedHours(): List<RequestedHourData>? {
    val request = getRequest(hoursUrl.build()) ?: return null

    if (request.status == HttpStatusCode.OK) return request.body()

    return null
}

suspend fun deleteRequestedHour(id: Int): Boolean {
    val request = getRequest(hoursUrl.build(), id) ?: return false

    return request.status == HttpStatusCode.OK
}

suspend fun patchRequestedHour(id: Int, approved: Boolean): Boolean {
    val request = patchRequest(hoursUrl.build(), HourPatchRequest(id, approved)) ?: return false

    return request.status == HttpStatusCode.OK
}