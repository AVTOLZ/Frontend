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

val accountsUrl = baseUrl.clone().appendPathSegments("accounts")
val loginUrl = accountsUrl.clone().appendPathSegments("login")
val magisterUrl = loginUrl.clone().appendPathSegments("magister")

val registerUrl = accountsUrl.clone().appendPathSegments("register")