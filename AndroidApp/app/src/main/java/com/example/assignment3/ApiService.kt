package com.example.assignment3

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface ApiService {

    @POST("login")
    fun loginUser(@Body credential: Map<String, String>) : Call<LoginRegisterResponse>

    @POST("register")
    fun registerUser(@Body userData: Map<String, String>) : Call<LoginRegisterResponse>

    @GET("getAllBooks")
    fun getAllBooks(@Header("x-access-token") authToken: String): Call<List<Book>>

    @POST("createBook")
    fun addBook(@Header("x-access-token") authToken: String, @Body movie: Book): Call<Void>

    @PUT("updateBook/{id}")
    fun updateBook(@Header("x-access-token") authToken: String, @Path("id") id: String, @Body movie: Book): Call<Void>

    @DELETE("deleteBookByID/{id}")
    fun deleteBook(@Header("x-access-token") authToken: String, @Path("id") id: String) : Call<Void>
}