package com.android.onlinesales.repository

import com.android.onlinesales.models.MathExpressionRequest
import com.android.onlinesales.models.MathExpressionResponse
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST


interface MathAPI {
    @POST("v4/")
    fun evaluateExpressions(@Body request: MathExpressionRequest): Call<MathExpressionResponse>
}
