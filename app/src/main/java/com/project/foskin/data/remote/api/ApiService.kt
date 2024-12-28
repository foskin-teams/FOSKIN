package com.project.foskin.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SendOtpRequest(
    @SerializedName("phone_number") val phoneNumber: String
)

data class SendOtpResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: OtpData?
)

data class OtpData(
    @SerializedName("otp_number") val otpNumber: String
)

data class SignInRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("otp_number") val otp: String
)

data class SignInResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TokenData?
)

data class TokenData(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)

data class SignOutResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)

interface ApiService {
    @POST("v1/send-otp")
    fun sendOtp(@Body request: SendOtpRequest): Call<SendOtpResponse>

    @POST("v1/sign-in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @POST("v1/sign-out")
    fun signOut(): Call<SignOutResponse>
}