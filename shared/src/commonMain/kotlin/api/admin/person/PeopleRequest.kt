package api.admin.person

import api.peopleUrl
import api.requests.getRequest
import io.ktor.client.call.*
import io.ktor.http.*

suspend fun getPeople(): List<PersonData>? {
    val request = getRequest(peopleUrl.build())

    if (request?.status == HttpStatusCode.OK) {
        return request.body()
    }

    return null
}
