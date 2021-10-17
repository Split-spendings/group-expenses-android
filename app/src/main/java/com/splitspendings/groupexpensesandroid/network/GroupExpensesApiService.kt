package com.splitspendings.groupexpensesandroid.network

import com.splitspendings.groupexpensesandroid.network.dto.GroupDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://10.0.2.2:8182"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GroupExpensesApiService {

    @GET("/test/groups")
    fun getAllGroups(): Call<List<GroupDto>>
}

object GroupExpensesApi {
    val retrofitService : GroupExpensesApiService by lazy {
        retrofit.create(GroupExpensesApiService::class.java)
    }
}