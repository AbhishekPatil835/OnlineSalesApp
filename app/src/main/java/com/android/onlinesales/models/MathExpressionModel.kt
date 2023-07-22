package com.android.onlinesales.models

import com.google.gson.annotations.SerializedName


class MathExpressionRequest(@field:SerializedName("expr") private val expressions: List<String>)

class MathExpressionResponse {
    @SerializedName("result")
    val results: List<String>? = null

    @SerializedName("error")
    val error: String? = null
}
