package api.person.magister

import Data
import api.magisterLinkUrl
import dev.tiebe.magisterapi.api.requestPOST
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun linkMagisterAccount(refreshToken: String): Boolean {
    val request = requestPOST(magisterLinkUrl.build(), requestBody = refreshToken, accessToken = Data.bearerToken)

    return request.status == HttpStatusCode.OK
}