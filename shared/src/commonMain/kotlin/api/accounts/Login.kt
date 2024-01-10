package api.accounts

import api.baseUrl
import api.client
import api.loginUrl
import api.magisterUrl
import api.requests.postRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

//TODO: error when logging in with incorrect information / account doesnt exist

suspend fun login(username: String, password: String): Boolean? {
    val request = postRequest(loginUrl.build(), LoginRequest(username, password)) ?: return null

    if (request.status == HttpStatusCode.OK) {
        val response = request.body<LoginResponse>()

        Data.bearerToken = response.token
        Data.personId = response.personId

        Data.verified = response.verified

        return true
    } else {
        return false
    }
}

suspend fun magisterLogin(refreshToken: String): Boolean? {
    val request = postRequest(magisterUrl.build(), refreshToken) ?: return null

    if (request.status == HttpStatusCode.OK) {
        val response = request.body<LoginResponse>()

        Data.personId = response.personId
        Data.bearerToken = response.token

        Data.verified = response.verified

        return true
    } else {
        return false
    }
}