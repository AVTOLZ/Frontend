package api.admin

import api.eventsUrl
import api.requests.getRequest
import io.ktor.client.call.*
import io.ktor.http.*

suspend fun getEvents(): List<Event>? {
    val request = getRequest(eventsUrl.build())

    if (request?.status == HttpStatusCode.OK) {
        return request.body()
    }

    return null
}