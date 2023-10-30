package api.person.info

import api.client
import api.infoUrl
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun readInfo() : Boolean {
    val request = client.get(infoUrl.build()) {
        contentType(ContentType.Application.Json)
        bearerAuth(Data.bearerToken ?: return false)
    }

    println(infoUrl.build())

    println(request.status)

    if (request.status == HttpStatusCode.OK) {
        val res = request.body<ReadInfoResponse>()
        Data.username = res.username
        Data.userFirstname = res.firstName
        Data.userLastname = res.lastName
        Data.userRank = res.rank.order
        Data.userRankString = res.rank.name
        return true
    }
    else {
        return false
    }
}