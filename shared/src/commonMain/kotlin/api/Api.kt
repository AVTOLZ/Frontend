package api

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

val baseUrl = URLBuilder("http://localhost:8080").appendPathSegments("api")

val accountsUrl = baseUrl.appendPathSegments("accounts")
val loginUrl = accountsUrl.appendPathSegments("login")
val magisterUrl = loginUrl.appendPathSegments("magister")