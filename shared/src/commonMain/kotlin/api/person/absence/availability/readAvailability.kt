package api.person.absence.availability

import api.availabilityUrl
import api.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun readAvailability() : List<AvailabilityItem>? {
    val request = client.get(availabilityUrl.build()) {
        contentType(ContentType.Application.Json)
        bearerAuth(Data.bearerToken ?: return null)
    }

    return if (request.status == HttpStatusCode.OK) {
        request.body<ReadAvailabilityResponse>().hours
    } else {
        null
    }
}