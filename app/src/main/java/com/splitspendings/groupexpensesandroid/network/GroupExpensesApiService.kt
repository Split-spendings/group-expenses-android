package com.splitspendings.groupexpensesandroid.network

import com.splitspendings.groupexpensesandroid.auth.AuthInterceptor
import com.splitspendings.groupexpensesandroid.network.dto.AppUserGroupsDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://se-back.thesis.net"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okhttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okhttpClient)
    .baseUrl(BASE_URL)
    .build()

interface GroupExpensesApiService {

    @GET("/test/groups")
    suspend fun getGroups(@Query("type") type: String): List<GroupDto>

    @GET("/api/users/groups")
    suspend fun appUserActiveGroups(): AppUserGroupsDto
}

object GroupExpensesApi {
    val retrofitService: GroupExpensesApiService by lazy {
        retrofit.create(GroupExpensesApiService::class.java)
    }
}