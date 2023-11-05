package api.requests

import api.client
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun postRequest(url: Url, body: Any?): HttpResponse? {
    return try {
        client.post(url) {
            contentType(ContentType.Application.Json)
            bearerAuth(Data.bearerToken ?: "")
            setBody(body ?: "")

            timeout {  }
        }
    } catch (error: Throwable) {
        null
    }
}