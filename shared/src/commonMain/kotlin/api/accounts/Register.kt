package api.accounts

import api.client
import api.loginUrl
import api.registerUrl
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun register(username: String, password: String, email: String, firstName: String, lastName: String): Boolean {
    val request = client.post(registerUrl.build()) {
        contentType(ContentType.Application.Json)
        setBody(RegisterRequest(
            username,
            password,
            email,
            firstName,
            lastName
        ))
    }

    if (request.status == HttpStatusCode.OK) {
        val response = request.body<LoginResponse>()

        Data.bearerToken = response.token
        Data.personId = response.personId

        return true
    } else {
        return false
    }
}