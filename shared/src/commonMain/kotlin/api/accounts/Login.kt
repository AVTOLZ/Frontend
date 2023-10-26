package api.accounts

import api.baseUrl
import api.client
import api.loginUrl
import api.magisterUrl
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun login(username: String, password: String): Boolean {
    val request = client.post(loginUrl.build()) {
        contentType(ContentType.Application.Json)
        setBody(LoginRequest(username, password))
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

suspend fun magisterLogin(refreshToken: String): Boolean {
    val request = client.post(magisterUrl.build()) {
        setBody(refreshToken)
    }

    if (request.status == HttpStatusCode.OK) {
        val response = request.body<LoginResponse>()

        Data.personId = response.personId
        Data.bearerToken = response.token

        return true
    } else {
        return false
    }
}