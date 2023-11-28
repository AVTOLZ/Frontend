package api.person.absence.present

import api.presentUrl
import api.requests.postRequest
import io.ktor.http.*

suspend fun announcePresence(id: Int, remove: Boolean) : HttpStatusCode? {
    val request = postRequest(presentUrl.build(), AnnouncePresenceRequest(id, remove)) ?: return null

    return request.status
}