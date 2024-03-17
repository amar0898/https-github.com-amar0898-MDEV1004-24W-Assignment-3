package com.example.assignment3

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing the response from login and registration requests.
 */
@JsonClass(generateAdapter = true)
data class LoginRegisterResponse(
    @Json(name = "_id") val id: String?,
    @Json(name = "firstname") val firstname: String?,
    @Json(name = "lastname") val lastname: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "token") val token: String?,

)
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "error") val error: String?
)

/**
 * Data class representing a book.
 */
data class Book(
    @Json(name = "_id") val id: String?,
    @Json(name = "BooksName") val bookName: String?,
    @Json(name ="ISBN") val isbn : String?,
    @Json(name ="Rating") val rating : Double,
    @Json(name ="Author") val author: String?,
    @Json(name ="Genre") val genre: String?

)
