package api.requests

import api.client
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getRequest(url: Url, body: Any? = null): HttpResponse? {
    return try {
        client.get(url) {
            if (Data.bearerToken != null) {
                bearerAuth(Data.bearerToken.toString())
            }
            if (body != null) {
                setBody(body)
            }
        }
    } catch (error: Throwable) {
        error.printStackTrace()
        null
    }
}