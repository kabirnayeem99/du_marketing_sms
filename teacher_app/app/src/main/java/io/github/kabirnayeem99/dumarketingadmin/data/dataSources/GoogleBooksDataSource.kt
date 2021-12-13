package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import io.github.kabirnayeem99.dumarketingadmin.data.dto.googleBooksDto.GoogleBooksResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_GOOGLE_BOOKS_API = "https://www.googleapis.com/"

interface GoogleBooksDataSource {
    @GET("books/v1/volumes")
    fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") limit: Int,
    ): Call<GoogleBooksResponseDto>
}