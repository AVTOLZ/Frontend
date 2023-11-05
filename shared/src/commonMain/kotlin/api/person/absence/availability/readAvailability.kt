package api.person.absence.availability

import api.availabilityUrl
import api.client
import api.requests.getRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun readAvailability() : List<AvailabilityItem> {
    val request = getRequest(availabilityUrl.build(), null) ?: return emptyList()

    return if (request.status == HttpStatusCode.OK) {
        request.body<ReadAvailabilityResponse>().hours
    }
    else {
        emptyList()
    }
}