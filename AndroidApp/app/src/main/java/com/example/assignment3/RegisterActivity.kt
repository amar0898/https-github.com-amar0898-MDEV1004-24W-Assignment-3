package com.example.assignment3

import android.content.DialogInterface
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
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidEmailString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }
    fun registerClicked(view: View){
        val firstnameText = findViewById<EditText>(R.id.firstnameEditText)
        val lastnameText = findViewById<EditText>(R.id.lastnameEditText)
        val emailText = findViewById<EditText>(R.id.emailEditText)
        val passwordText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordText = findViewById<EditText>(R.id.confirmPasswordEditText)

        if (emailText.text.isEmpty() || passwordText.text.isEmpty()){
            val builder = AlertDialog.Builder(this@RegisterActivity)
            builder.setMessage("username, email or password cannot be empty")
            builder.setTitle("Registration Failed")
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                    dialog?.cancel()
                })
            val alertDialog = builder.create()
            runOnUiThread(java.lang.Runnable {alertDialog.show()})
        }
        else if(!isValidEmailString(emailText.text.toString())){

            val builder = AlertDialog.Builder(this@RegisterActivity)
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
        else{
            try{
                val userData = mapOf(
                    "email" to emailText.text.toString(),
                    "firstname" to firstnameText.text.toString(),
                    "lastname" to lastnameText.text.toString(),
                    "password" to passwordText.text.toString()
                )
                val service = RetrofitClient.retrofit.create(ApiService::class.java)
                GlobalScope.launch {
                    //password validation
                    if(passwordText.text.toString() == confirmPasswordText.text.toString()){
                        //register user
                        service.registerUser(userData).enqueue(object :
                            Callback<LoginRegisterResponse> {
                            override fun onResponse(
                                call: Call<LoginRegisterResponse>,
                                response: Response<LoginRegisterResponse>
                            ) {
                                val registerResponse = response.body()
                                if (registerResponse != null) {

                                    Toast.makeText(this@RegisterActivity, "Registration Success", Toast.LENGTH_SHORT).show()
                                    println("Registration Success")
                                    finish()


                                } else {
                                    val moshi = Moshi.Builder().build()
                                    val adapter: JsonAdapter<ErrorResponse> =
                                        moshi.adapter(ErrorResponse::class.java)
                                    val errorResponse = adapter.fromJson(response.errorBody()!!.source())

                                    val errorMessage = errorResponse?.error ?: "Unknown error"
                                    val builder = AlertDialog.Builder(this@RegisterActivity)
                                    builder.setMessage(errorMessage)
                                    builder.setTitle("Registration Failed")
                                    builder.setPositiveButton("OK",
                                        DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                                            dialog?.cancel()
                                        })
                                    runOnUiThread(java.lang.Runnable {val alertDialog = builder.create()
                                        alertDialog.show()})
                                }

                            }

                            override fun onFailure(call: Call<LoginRegisterResponse>, t: Throwable) {
                                val builder = AlertDialog.Builder(this@RegisterActivity)
                                builder.setMessage("unknown error, try again later")
                                builder.setTitle("Registration Failed")
                                builder.setPositiveButton("OK",
                                    DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                                        dialog?.cancel()
                                    })
                                runOnUiThread(java.lang.Runnable {val alertDialog = builder.create()
                                    alertDialog.show()})
                            }
                        })



                    } else {
                        // alert password mismatch
                        val builder = AlertDialog.Builder(this@RegisterActivity)
                        builder.setMessage("Passwords do not match.")
                        builder.setTitle("Registration Failed")
                        builder.setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                                dialog?.cancel()

                                //clear fields and set focus
                                runOnUiThread(java.lang.Runnable {
                                    passwordText.setText("")
                                    confirmPasswordText.setText("")
                                    passwordText.requestFocus()
                                    val inputManager =
                                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                    inputManager.showSoftInput(
                                        emailText,
                                        InputMethodManager.SHOW_IMPLICIT
                                    )
                                })
                            })
                        runOnUiThread(java.lang.Runnable {val alertDialog = builder.create()
                            alertDialog.show()})
                    }


                }
            }catch (e:Exception){
                println("Failed to Register User")
            }
        }
    }
    fun cancelClicked(view: View) {
        finish()
    }
}

