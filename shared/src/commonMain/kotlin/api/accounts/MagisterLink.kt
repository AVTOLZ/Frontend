package api.accounts

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun magisterLink(bearer: String, personId: Int, refreshToken: String): HttpStatusCode {
    val client = HttpClient()

    val request = client.post("http://localhost:8080/api/person/$personId/magister") {
        header("Authorization", "Bearer $bearer")

        setBody(refreshToken)
    }

    return request.status
}