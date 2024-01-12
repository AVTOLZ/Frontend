package api.admin.Event

import api.eventsUrl
import api.requests.deleteRequest
import api.requests.getRequest
import api.requests.postRequest
import api.requests.putRequest
import io.ktor.client.call.*
import io.ktor.http.*

suspend fun getEvents(): List<Event>? {
    val request = getRequest(eventsUrl.build())

    if (request?.status == HttpStatusCode.OK) {
        return request.body()
    }

    return null
}

suspend fun postEvent(body: Event): Boolean {
    val request = postRequest(eventsUrl.build(), body) ?: return false

    return request.status == HttpStatusCode.OK
}

suspend fun deleteEvent(body: Int): Boolean {
    val request = deleteRequest(eventsUrl.build(), body) ?: return false

    return request.status == HttpStatusCode.OK
}

suspend fun putEvent(body: Event): Boolean {
    val request = putRequest(eventsUrl.build(), body) ?: return false

    return request.status == HttpStatusCode.OK
}