package api.person.info

import api.client
import api.infoUrl
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun ReadInfo() : ReadInfoResponse? {
    val request = client.get(infoUrl.build()) {
        contentType(ContentType.Application.Json)
        setBody(ReadInfoRequest(Data.bearerToken.toString()))
    }

    if (request.status == HttpStatusCode.OK) {
        return request.body<ReadInfoResponse>()
    }
    else {
        return null
    }
}