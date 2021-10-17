package com.splitspendings.groupexpensesandroid.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://10.0.2.2:8182"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface GroupExpensesApiService {

    @GET("/test/groups")
    fun getAllGroups(): Call<String>
}

object GroupExpensesApi {
    val retrofitService : GroupExpensesApiService by lazy {
        retrofit.create(GroupExpensesApiService::class.java)
    }
}