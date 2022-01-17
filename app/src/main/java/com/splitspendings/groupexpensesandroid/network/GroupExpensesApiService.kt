package com.splitspendings.groupexpensesandroid.network

import com.splitspendings.groupexpensesandroid.auth.AuthInterceptor
import com.splitspendings.groupexpensesandroid.common.GroupsFilter
import com.splitspendings.groupexpensesandroid.network.dto.AppUserDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupActiveMembersDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupBalancesDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupInviteCodeDto
import com.splitspendings.groupexpensesandroid.network.dto.GroupSpendingsDto
import com.splitspendings.groupexpensesandroid.network.dto.NewGroupDto
import com.splitspendings.groupexpensesandroid.network.dto.NewPayoffDto
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.network.dto.PayoffDto
import com.splitspendings.groupexpensesandroid.network.dto.SpendingDto
import com.splitspendings.groupexpensesandroid.network.dto.SpendingShortDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @GET("/api/groups/{groupId}/spendings")
    suspend fun groupSpendings(@Path("groupId") groupId: Long): GroupSpendingsDto

    @POST("/api/groups/invites/code/{groupId}")
    suspend fun getOrGenerateGroupInviteCode(@Path("groupId") groupId: Long): GroupInviteCodeDto

    @POST("/api/groups/invites/generate/{groupId}")
    suspend fun generateGroupInviteCode(@Path("groupId") groupId: Long): GroupInviteCodeDto

    @POST("/api/groups/invites/join/{code}")
    suspend fun joinGroupByInviteCode(@Path("code") code: String): GroupDto

    @GET("/api/balances/{groupId}")
    suspend fun groupBalances(@Path("groupId") groupId: Long): GroupBalancesDto

    @POST("/api/spendings")
    suspend fun createSpending(@Body newSpending: NewSpendingDto): SpendingShortDto

    @GET("/api/groups/{groupId}/members")
    suspend fun groupActiveMembers(@Path("groupId") groupId: Long): GroupActiveMembersDto

    @PATCH("/api/groups/{groupId}/leave")
    suspend fun leaveGroup(@Path("groupId") groupId: Long)

    @GET("/api/spendings/{spendingId}")
    suspend fun spendingById(@Path("spendingId") spendingId: Long): SpendingDto

    @DELETE("/api/spendings/{spendingId}")
    suspend fun deleteSpending(@Path("spendingId") spendingId: Long)

    @GET("/api/groups/{groupId}/payoffs")
    suspend fun groupPayoffs(@Path("groupId") groupId: Long): List<PayoffDto>

    @DELETE("/api/payoffs/{payoffId}")
    suspend fun deletePayoff(@Path("payoffId") payoffId: Long)

    @POST("/api/payoffs")
    suspend fun createPayoff(@Body newPayoffDto: NewPayoffDto): PayoffDto

    @GET("/api/users/current")
    suspend fun profileShort(): AppUserDto
}

object GroupExpensesApi {
    val retrofitService: GroupExpensesApiService by lazy {
        retrofit.create(GroupExpensesApiService::class.java)
    }
}