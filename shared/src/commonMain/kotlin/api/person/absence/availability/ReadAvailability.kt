package api.person.absence.availability

import api.availabilityUrl
import api.client
import dev.avt.api.person.absence.availability.HourDataFormat
import dev.avt.api.person.absence.availability.ReadAvailabilityResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun ReadAvailability() : List<HourDataFormat>? {
    val request = client.get(availabilityUrl.build()) {
        contentType(ContentType.Application.Json)
        setBody(ReadAvailabilityRequest(Data.bearerToken.toString()))
    }

    if (request.status == HttpStatusCode.OK) {
        return request.body<ReadAvailabilityResponse>().hours
    }
    else {
        return null
    }
}