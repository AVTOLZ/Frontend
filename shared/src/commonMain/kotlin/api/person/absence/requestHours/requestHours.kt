package api.person.absence.requestHours

import api.client
import api.requestHoursUrl
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun requestHours(idList: List<Int>): Boolean {
    val request = client.post(requestHoursUrl.build()) {
        contentType(ContentType.Application.Json)
        bearerAuth(Data.bearerToken ?: return false)
        setBody(RequestHoursRequest(idList))
    }

    return request.status == HttpStatusCode.OK
}