package com.example.assignment3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), BookAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchMovies()

    }

    override fun onResume() {
        super.onResume()

        fetchMovies()

    }


    public fun fetchMovies() {

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AuthToken", null)

        // Check if the authToken is not null before using it
        /*if (authToken.isNullOrEmpty()) {
            println("token null")
            return
        }*/
        val service = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = service.getAllBooks(authToken!!)

        call.enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (!movies.isNullOrEmpty()) {
                        adapter = BookAdapter(movies)
                        recyclerView.adapter = adapter

                        val swipeToDeleteCallback = SwipeGesture(this@MainActivity, adapter)
                        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                        itemTouchHelper.attachToRecyclerView(recyclerView)

                        adapter.setOnItemClickListener(this@MainActivity)

                    } else {
                        displayErrorMessage("No movies available.")
                    }
                } else {
                    displayErrorMessage("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                displayErrorMessage("Error: ${t.message}")
            }
        })


    }

    private fun displayErrorMessage(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    fun addMovieClicked(view: View) {
        val i = Intent(this@MainActivity, AddEditBookActivity::class.java)
        startActivity(i);
    }

    override fun onItemClick(model: Book) {
        val i = Intent(this@MainActivity, AddEditBookActivity::class.java)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<Book>(Book::class.java!!)
        val movie = jsonAdapter.toJson(model)
        i.putExtra("AddEditKey", movie)
        startActivity(i);
    }

    fun logoutClicked(view: View) {

        //clear auth token
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("AuthToken").apply()

        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i);
        finish()
    }
}

