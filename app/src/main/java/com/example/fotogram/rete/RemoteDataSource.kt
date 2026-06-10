package com.example.fotogram.rete

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.http.isSuccess
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.request.parameter

class RemoteDataSource(
    private val client: HttpClient = KtorClient.httpClient
) {

    suspend fun loginUtente(): LoginResponse {
        val response = client.post("${KtorClient.BASE_URL}user")

        Log.d("RemoteDataSource", "Status risposta login: ${response.status}")

        if (response.status.isSuccess()) {
            val loginResponse: LoginResponse = response.body()

            Log.d("RemoteDataSource", "SessionId ricevuto: ${loginResponse.sessionId}")
            Log.d("RemoteDataSource", "UserId ricevuto: ${loginResponse.userId}")

            return response.body()
        } else {
            val errore: ErrorResponse = response.body()
            Log.d("RemoteDataSource", "Errore server: ${errore.message}")
            throw Exception(errore.message)
        }
    }

    suspend fun caricaUtente(
        sessionId: String,
        userId: Int
    ): UserResponse {
        val response = client.get("${KtorClient.BASE_URL}user/$userId") {
            headers {
                append("x-session-id", sessionId)
            }
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errore: ErrorResponse = response.body()
            throw Exception(errore.message)
        }
    }

    //PUT user
    suspend fun aggiornaUtente(
        sessionId: String,
        request: AggiornaUtenteRequest
    ): UserResponse {
        val response = client.put("${KtorClient.BASE_URL}user") {
            headers {
                append("x-session-id", sessionId)
            }

            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errore: ErrorResponse = response.body()
            throw Exception(errore.message)
        }
    }

    //GET feed
    suspend fun caricaFeed(
        sessionId: String,
        maxPostId: Int? = null,
        limit: Int = 10,
        seed: Int? = null
    ): List<Int> {
        val response = client.get("${KtorClient.BASE_URL}feed") {
            headers {
                append("x-session-id", sessionId)
            }

            maxPostId?.let {
                parameter("maxPostId", it)
            }

            parameter("limit", limit)

            seed?.let {
                parameter("seed", it)
            }
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errore: ErrorResponse = response.body()
            throw Exception(errore.message)
        }
    }

    //GET post-postId
    suspend fun caricaPost(
        sessionId: String,
        postId: Int
    ): PostResponse {
        val response = client.get("${KtorClient.BASE_URL}post/$postId") {
            headers {
                append("x-session-id", sessionId)
            }
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errore: ErrorResponse = response.body()
            throw Exception(errore.message)
        }
    }
}