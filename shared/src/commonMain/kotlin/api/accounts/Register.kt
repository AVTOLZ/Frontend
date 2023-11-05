package api.accounts

import api.client
import api.loginUrl
import api.registerUrl
import api.requests.postRequest
import api.verifyUrl
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun register(username: String, password: String, email: String, firstName: String, lastName: String): Boolean? {
    val request = postRequest(registerUrl.build(), RegisterRequest(
        username,
        password,
        email,
        firstName,
        lastName
    )) ?: return false

    if (request.status == HttpStatusCode.OK) {
        val response = request.body<LoginResponse>()

        Data.bearerToken = response.token
        Data.personId = response.personId

        Data.verified = response.verified

        return true
    } else if (request.status == HttpStatusCode.Conflict) {
        return null
    } else {
        return false
    }
}

suspend fun verifyAccount(personId: Int, code: Int): Boolean? {
    val request = try {
        client.post(verifyUrl.build()) {
            parameter("id", personId)
            parameter("code", code)
        }
    }
    catch (error: Throwable) {
        return null
    }

    if (request.status == HttpStatusCode.OK) {
        Data.verified = true
        return true
    }

    return false
}