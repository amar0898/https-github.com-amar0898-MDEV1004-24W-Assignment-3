/*
 * AddEditBookActivity.kt - Activity for adding or editing a book
 *
 * This activity allows users to add a new book or edit an existing one. It receives book data
 * from the intent and populates the UI accordingly. Users can input or modify book details such as
 * title, ISBN, author, genres, and rating. When users click on the "Add" or "Update" button,
 * the corresponding method is called to add or update the book via Retrofit API calls.
 *
 * Author: Yamuna Ravi Thalakatt,Amardeep Amardeep
 */
package com.example.assignment3

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEditBookActivity : AppCompatActivity() {

    // UI elements
    private lateinit var titleText : EditText
    private lateinit var isbnText : EditText
    private lateinit var authorText : EditText
    private lateinit var genresText : EditText
    private lateinit var ratingText : EditText
    private var book : Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_book)

        // Initialize UI elements and retrieve book data from intent
        var updateButton = findViewById<Button>(R.id.updateButton)
        var addEditText =  findViewById<TextView>(R.id.AddEditText)
        titleText = findViewById<EditText>(R.id.titleText)
        isbnText = findViewById<EditText>(R.id.isbnText)
        genresText = findViewById<EditText>(R.id.genresText)
        authorText = findViewById<EditText>(R.id.authorText)
        ratingText = findViewById<EditText>(R.id.ratingText)

        // Receive book data from intent
        val jsonString = intent.getStringExtra("AddEditKey")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<Book>(Book::class.java!!)

        // Populate UI elements with book data if available
        if(!jsonString.isNullOrEmpty()){
            book = jsonAdapter.fromJson(jsonString)
        }

        if(book != null){
            // If editing an existing book, set appropriate UI elements
            updateButton.text = "Update"
            addEditText.text = "Edit Book"

            titleText.setText(book?.bookName)
            isbnText.setText(book?.isbn)
            genresText.setText(book?.genre)
            ratingText.setText(book?.rating.toString())
            authorText.setText(book?.author)
        }
        else{
            // If adding a new book, set appropriate UI elements
            updateButton.text = "Add"
            addEditText.text = "Add Book"
        }
    }


    // Function to handle click events on the "Add" or "Update" button
    fun addOrUpdateClicked(view: View) {

        val service = RetrofitClient.retrofit.create(ApiService::class.java)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AuthToken", null)

        // Check if the authToken is not null before using it
        if (authToken.isNullOrEmpty()) {
            println("token null at add edit")
            return
        }

        if(book != null) {
            //do update
            GlobalScope.launch{
                //call put request
                val bookData = Book(
                    id = book?.id!!,
                    bookName = titleText.text.toString(),
                    isbn = isbnText.text.toString(),
                    genre= genresText.text.toString(),
                    author = authorText.text.toString(),
                    rating =ratingText.text.toString().toDouble()
                    )

                service.updateBook(authToken!! ,book?.id!!, bookData).enqueue(object :
                    Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            displayErrorMessage("Book details updated successfully" , "Success")
                        }
                        else{
                            displayErrorMessage("Book details update failed" , "Failed")

                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        displayErrorMessage("Error- " + t.message , "Error")
                        println("error: " +  t.message)
                    }

                })
            }
        }else{
            //do add

            GlobalScope.launch{
                //call post request

                val bookData = Book(
                    id = null,
                    bookName = titleText.text.toString(),
                    isbn = isbnText.text.toString(),
                    genre= genresText.text.toString(),
                    author = authorText.text.toString(),
                    rating =ratingText.text.toString().toDouble()
                )

                service.addBook(authToken!!,bookData).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            displayErrorMessage("Book Added Successfully" , "Success")
                        }
                        else{
                            displayErrorMessage("Book Addition Failed" , "Fail")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        displayErrorMessage("Error- " + t.message , "Error")
                    }
                })
            }
        }


    }

    fun cancelClicked(view: View) {
        finish()
    }

    private fun displayErrorMessage(message: String, title: String) {
        // Build an alert dialog to display the error message
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog: DialogInterface?, _: Int ->
                finish()
            })
            .show()
    }
}

