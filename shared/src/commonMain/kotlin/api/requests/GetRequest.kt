package api.requests

import api.client
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getRequest(url: Url, body: Any?): HttpResponse? {
    return try {
        client.get(url) {
            contentType(ContentType.Application.Json)
            bearerAuth(Data.bearerToken ?: "")
            setBody(body ?: "")
        }
    } catch (error: Throwable) {
        null
    }
}