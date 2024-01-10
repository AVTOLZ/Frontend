package api

import Data
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 5000
    }
}

val PROD_URL = URLBuilder("https://avt.tiebe.dev").appendPathSegments("api")
val DEBUG_URL = URLBuilder("http://localhost:8080").appendPathSegments("api")
val baseUrl = DEBUG_URL

val accountsUrl = baseUrl.clone().appendPathSegments("accounts")
val loginUrl = accountsUrl.clone().appendPathSegments("login")
val magisterUrl = loginUrl.clone().appendPathSegments("magister")

val personUrl get() = baseUrl.clone().appendPathSegments("person", Data.personId.toString())
val availabilityUrl get() = personUrl.clone().appendPathSegments("availability")
val requestHoursUrl get() = personUrl.clone().appendPathSegments("request_hours")

val registerUrl = accountsUrl.clone().appendPathSegments("register")
val verifyUrl = accountsUrl.clone().appendPathSegments("verify")

val infoUrl get() = personUrl.clone().appendPathSegments("info")

val presentUrl get() = personUrl.clone().appendPathSegments("announce_presence")
val magisterLinkUrl get() = personUrl.clone().appendPathSegments("magister")

val adminUrl = baseUrl.clone().appendPathSegments("admin")
val eventsUrl = adminUrl.clone().appendPathSegments("events")