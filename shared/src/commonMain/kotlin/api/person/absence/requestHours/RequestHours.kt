package api.person.absence.requestHours

import api.client
import api.requestHoursUrl
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun RequestHours(IdList: List<Int>): Boolean {
    val request = client.post(requestHoursUrl.build()) {
        contentType(ContentType.Application.Json)
        setBody(RequestHoursRequest(Data.bearerToken.toString(), IdList))
    }

    if (request.status == HttpStatusCode.OK) {
        return true
    }
    else {
        return false
    }
}