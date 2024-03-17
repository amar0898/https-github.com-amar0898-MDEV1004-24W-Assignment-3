/*
 * LoginActivity.kt
 * This activity allows users to log in to the application using their email and password.
 * It performs validation on user input and communicates with the server to authenticate users.
 * Author: Yamuna Ravi Thalakatt,Amardeep Amardeep
 */
package com.example.assignment3

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    lateinit var emailText : EditText
    lateinit var passwordText : EditText

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize EditText fields
        emailText = findViewById(R.id.emailLoginEditText)
        passwordText = findViewById(R.id.passwordEditText)
    }

    fun clearLoginEditText(){

        // Clear EditText fields and request focus
        emailText.setText("")
        passwordText.setText("")
        emailText.requestFocus()
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(emailText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun isValidEmailString(str: String): Boolean{
        // Validate email format using a regular expression
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }
    fun loginClicked(view: View) {
        if(emailText.text.isEmpty() || passwordText.text.isEmpty()){

            // Show alert if username or password is empty
            val builder = AlertDialog.Builder(this@LoginActivity)
            builder.setMessage("username and password are required")
            builder.setTitle("Login Failed")
            builder.setPositiveButton("ok",
                DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->

                    dialog?.cancel()

                    if(emailText.text.isEmpty()){
                        emailText.requestFocus()
                        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.showSoftInput(emailText, InputMethodManager.SHOW_IMPLICIT)
                    }else if(passwordText.text.isEmpty()){
                        passwordText.requestFocus()
                        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.showSoftInput(passwordText, InputMethodManager.SHOW_IMPLICIT)
                    }
                })
            val alertDialog = builder.create()
            runOnUiThread(java.lang.Runnable {alertDialog.show()})

        }
        else if(!isValidEmailString(emailText.text.toString())){

            // Show alert if email format is invalid
            val builder = AlertDialog.Builder(this@LoginActivity)
            builder.setMessage("Invalid Email")
            builder.setTitle("Login Failed")
            builder.setPositiveButton("ok",
                DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                    dialog?.cancel()

                    //focus shift after alert is close
                    emailText.requestFocus()
                    val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.showSoftInput(emailText, InputMethodManager.SHOW_IMPLICIT)
                })
            val alertDialog = builder.create()
            runOnUiThread(java.lang.Runnable {alertDialog.show()})
        }
        else {
            try {

                val credentials = HashMap<String, String>().apply {
                    put("email", emailText.text.toString())
                    put("password", passwordText.text.toString())
                }

                // Perform login process
                val service = RetrofitClient.retrofit.create(ApiService::class.java)
                GlobalScope.launch {
                    service.loginUser(credentials).enqueue(object : Callback<LoginRegisterResponse> {
                        override fun onResponse(
                            call: Call<LoginRegisterResponse>,
                            response: Response<LoginRegisterResponse>,

                        ) {
                            if (response.body() != null) {
                                val loginResponse = response.body()
                                if (!loginResponse!!.token.isNullOrBlank()) {

                                    // Save the token in SharedPreferences or other local storage
                                    val sharedPreferences =
                                        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                    sharedPreferences.edit()
                                        .putString("AuthToken", loginResponse.token).apply()
                                    println("User logged in successfully.")
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Login Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Proceed to next activity
                                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(i);
                                    clearLoginEditText()
                                    finish()

                                }
                            }
                            else{
                                val moshi = Moshi.Builder().build()
                                val adapter: JsonAdapter<ErrorResponse> =
                                    moshi.adapter(ErrorResponse::class.java)
                                val errorResponse = adapter.fromJson(response.errorBody()!!.source())
                                    val errorMessage = errorResponse!!.error

                                    val builder = AlertDialog.Builder(this@LoginActivity)
                                    builder.setMessage(errorMessage)
                                    builder.setTitle("Login Failed")
                                    builder.setPositiveButton("OK",
                                        DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                                            dialog?.cancel()
                                        })
                                    runOnUiThread(java.lang.Runnable {
                                        val alertDialog = builder.create()
                                        alertDialog.show()
                                    })

                            }
                        }


                        override fun onFailure(call: Call<LoginRegisterResponse>, t: Throwable) {
                            println("Failed to send request: ${t.message}")

                        }
                    })
                }
            }catch (e: Exception){
                println("failed to login " + e.message)
            }
        }
    }
    fun registerNowClicked(view: View) {
        // Navigate to RegisterActivity when Register Now is clicked
        val i = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(i);
        clearLoginEditText()
    }
}