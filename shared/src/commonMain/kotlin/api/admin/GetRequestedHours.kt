package api.admin

import api.hoursUrl
import api.requests.getRequest
import io.ktor.client.call.*
import io.ktor.http.*

suspend fun getRequestedHours(): List<RequestedHour>? {
    val request = getRequest(hoursUrl.build())

    if (request?.status == HttpStatusCode.OK) {
        return request.body()
    }

    return null
}