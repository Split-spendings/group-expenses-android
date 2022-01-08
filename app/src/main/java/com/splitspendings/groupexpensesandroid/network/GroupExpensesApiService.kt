package com.splitspendings.groupexpensesandroid.network

import com.splitspendings.groupexpensesandroid.auth.AuthInterceptor
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.network.dto.GroupBalancesDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupSpendingsDto
import com.splitspendings.groupexpensesandroid.network.dto.NewGroupDto
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.network.dto.SpendingDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://split-expenses-back.herokuapp.com"

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

    @GET("/api/groups/filter/{filter}")
    suspend fun getFilteredGroups(@Path("filter") filter: GroupsFilter): List<GroupDto>

    @POST("/api/groups")
    suspend fun createGroup(@Body newGroup: NewGroupDto): GroupDto

    @GET("/api/groups/{id}/spendings")
    suspend fun groupSpendings(@Path("id") groupId: Long): GroupSpendingsDto

    @POST("/api/groups/join/{code}")
    suspend fun joinGroup(@Path("code") code: String): GroupDto

    @GET("/api/balances/{id}")
    suspend fun groupBalances(@Path("id") groupId: Long): GroupBalancesDto

    @POST("/api/spendings")
    suspend fun createSpending(@Body newSpending: NewSpendingDto): SpendingDto
}

object GroupExpensesApi {
    val retrofitService: GroupExpensesApiService by lazy {
        retrofit.create(GroupExpensesApiService::class.java)
    }
}