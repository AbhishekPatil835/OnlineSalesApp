package com.android.onlinesales.android

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.onlinesales.viewmodel.MainViewModel
import com.android.onlinesales.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var editText: TextInputEditText
    private lateinit var resultTextView: TextView
    private lateinit var historyImageView: ImageView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        editText = findViewById(R.id.textInputEdittextLayoutExpressions)
        resultTextView = findViewById(R.id.text_view_results)
        resultTextView.text = ""
        historyImageView = findViewById(R.id.imageViewHistory)

        historyImageView.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        editText.doAfterTextChanged {
            resultTextView.text = ""

        }

        val evaluateButton: Button = findViewById(R.id.button_evaluate)
        evaluateButton.setOnClickListener {
            if(editText.text?.isNotEmpty() == true){
                progressDialog.show()
                evaluateExpressions()
            }

        }


    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun evaluateExpressions() {
        val input = editText.text.toString()
        val expressions = input.split("\n")
        mainViewModel.evaluateExpressions(expressions)
        // Temporarily save history item with an empty result until the API response arrives.
    }

    override fun onResume() {
        super.onResume()
        observeResultLiveData()
    }

    private fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun observeResultLiveData() {
        mainViewModel.getResultLiveData().observe(this, Observer { result ->

            hideProgressDialog()
            resultTextView.text = result

            mainViewModel.saveHistoryItem(result, getCurrentDateTime())
        })
    }

    // Override onPause to remove the LiveData observer when the activity is paused
    override fun onPause() {
        super.onPause()
        mainViewModel.getResultLiveData().removeObservers(this)
    }
}
