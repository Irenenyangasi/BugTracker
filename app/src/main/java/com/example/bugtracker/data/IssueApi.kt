package com.example.bugtracker.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IssueApi {

    @GET("issues")
    suspend fun getIssues(): List<Issue>

    @POST("issues")
    suspend fun createIssue(@Body issue: Issue): Issue

    @PUT("issues/{id}")
    suspend fun updateIssue(
        @Path("id") id: Int,
        @Body issue: Issue
    ): Issue

    @DELETE("issues/{id}")
    suspend fun deleteIssue(
        @Path("id") id: Int
    )
}