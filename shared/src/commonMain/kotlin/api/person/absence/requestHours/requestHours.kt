package api.person.absence.requestHours

import api.client
import api.requestHoursUrl
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun requestHours(hourId: Int, requestType: HourRequestType): Boolean {
    val request = client.post(requestHoursUrl.build()) {
        contentType(ContentType.Application.Json)
        bearerAuth(Data.bearerToken ?: return false)
        setBody(RequestHoursRequest(hourId, requestType))
    }

    return request.status == HttpStatusCode.OK
}