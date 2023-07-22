package com.android.onlinesales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.onlinesales.ApplicationClass
import com.android.onlinesales.database.AppDatabase
import com.android.onlinesales.models.MathExpressionRequest
import com.android.onlinesales.models.MathExpressionResponse
import com.android.onlinesales.repository.HistoryItem
import com.android.onlinesales.repository.MathAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    private val mathAPI: MathAPI = createMathAPI()
    private val resultLiveData: MutableLiveData<String> = MutableLiveData()
    private val database: AppDatabase? = AppDatabase.getDatabase(ApplicationClass.getInstance())

    private fun createMathAPI(): MathAPI {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // You can use Level.BASIC for less verbose logs.
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.mathjs.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(MathAPI::class.java)
    }
    fun getResultLiveData(): LiveData<String> {
        return resultLiveData
    }
    fun evaluateExpressions(expressions: List<String>) {
        val request = MathExpressionRequest(expressions)
        val call: Call<MathExpressionResponse> = mathAPI.evaluateExpressions(request)
        call.enqueue(object : Callback<MathExpressionResponse> {
            override fun onResponse(
                call: Call<MathExpressionResponse>,
                response: Response<MathExpressionResponse>
            ) {
                if (response.isSuccessful) {
                    val mathExpressionResponse = response.body()
                    if (mathExpressionResponse != null) {
                        val results = mathExpressionResponse.results
                        val stringBuilder = StringBuilder()
                        for (i in expressions.indices) {
                            stringBuilder.append("${expressions[i]} => ${results?.get(i)}\n")
                        }
                        resultLiveData.postValue(stringBuilder.toString())
                    } else {
                        resultLiveData.postValue("Response body is empty or null.")
                    }
                } else {
                    resultLiveData.postValue("Error occurred while evaluating expressions.")
                }
            }

            override fun onFailure(call: Call<MathExpressionResponse>, t: Throwable) {
                resultLiveData.postValue("Error occurred while communicating with the API.")
            }
        })
    }


    fun saveHistoryItem(result: String,submissionDate:String) {
        val historyItem = HistoryItem(result = result, submissionDate = submissionDate)
        viewModelScope.launch(Dispatchers.IO) {
            database?.historyItemDao()?.insertHistoryItem(historyItem)
        }
    }

    fun getAllHistoryItems(): LiveData<List<HistoryItem>> {
        val allItemsLiveData: MutableLiveData<List<HistoryItem>> = MutableLiveData()
        viewModelScope.launch(Dispatchers.IO) {
            val historyItems = database?.historyItemDao()?.getAllHistoryItems()
            withContext(Dispatchers.Main) {
                allItemsLiveData.postValue(historyItems)
            }
        }
        return allItemsLiveData
    }


}
