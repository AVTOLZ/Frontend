package api.accounts

import Data
import api.registerUrl
import api.requests.postRequest
import io.ktor.client.call.*
import io.ktor.http.*

suspend fun register(username: String, password: String, email: String, firstName: String, lastName: String, studentId: Int): Boolean? {
    val request = postRequest(registerUrl.build(), RegisterRequest(
        username,
        password,
        email,
        firstName,
        lastName,
        studentId
    )) ?: return false

    when (request.status) {
        HttpStatusCode.OK -> {
            val response = request.body<LoginResponse>()

            Data.bearerToken = response.token
            Data.personId = response.personId

            Data.verified = response.verified

            return true
        }
        HttpStatusCode.Conflict -> {
            return null
        }
        else -> {
            return false
        }
    }
}