package api.requests

import Data
import api.client
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun putRequest(url: Url, body: Any? = null): HttpResponse? {
    return try {
        client.put(url) {
            contentType(ContentType.Application.Json)

            if (Data.bearerToken != null) bearerAuth(Data.bearerToken.toString())

            if (body != null) setBody(body)

            timeout {  }
        }
    } catch (error: Throwable) {
        error.printStackTrace()
        null
    }
}