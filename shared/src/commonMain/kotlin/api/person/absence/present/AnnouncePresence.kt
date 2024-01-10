package api.person.absence.present

import api.presentUrl
import api.requests.deleteRequest
import api.requests.postRequest
import io.ktor.http.*

suspend fun announcePresence(id: Int, remove: Boolean) : HttpStatusCode? {
    val request = if (remove) {
        deleteRequest(presentUrl.build(), AnnouncePresenceRequest(id)) ?: return null
    }
    else {
        postRequest(presentUrl.build(), AnnouncePresenceRequest(id)) ?: return null
    }

    return request.status
}