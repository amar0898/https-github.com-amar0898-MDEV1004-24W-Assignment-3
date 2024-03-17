package com.example.assignment3

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipeGesture(private val context: Context, private val adapter: BookAdapter) :
    ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT
) {
    private val deleteBackgroundColor = ContextCompat.getColor(context, R.color.red)
    private val deleteTextColor = ContextCompat.getColor(context, R.color.white)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val paint = Paint()

        if (dX > 0) {
            paint.color = deleteBackgroundColor
            val background = RectF(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                dX,
                itemView.bottom.toFloat()
            )
            c.drawRect(background, paint)

            paint.color = deleteTextColor
            paint.textSize = 48f
            val text = "Delete"
            paint.typeface = Typeface.DEFAULT_BOLD
            val textWidth = paint.measureText(text)
            val textX = itemView.right.toFloat() + (dX - textWidth) / 2
            val textY = itemView.top.toFloat() + itemView.height / 2 - (paint.descent() + paint.ascent()) / 2
            c.drawText(text, textX, textY, paint)
        } else {
            paint.color = deleteBackgroundColor
            val background = RectF(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            c.drawRect(background, paint)

            paint.color = deleteTextColor
            paint.textSize = 48f
            paint.typeface = Typeface.DEFAULT_BOLD
            val text = "Delete"
            val textWidth = paint.measureText(text)
            val textX = itemView.right.toFloat() + (dX - textWidth) / 2
            val textY = itemView.top.toFloat() + itemView.height / 2 - (paint.descent() + paint.ascent()) / 2
            c.drawText(text, textX, textY, paint)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        val book = adapter.getItem(position)

        when (direction) {
            ItemTouchHelper.LEFT -> {

                showDeleteConfirmationDialog { confirmed ->

                    if (confirmed) {

                        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val authToken = sharedPreferences.getString("AuthToken", null)

                        // Check if the authToken is not null before using it
                        if (authToken.isNullOrEmpty()) {
                            println("token null at delete")

                        }
                        else{
                            GlobalScope.launch {
                                //remove movie, call delete request
                                val service = RetrofitClient.retrofit.create(ApiService::class.java)
                                service.deleteBook(authToken!!,book.id!!).enqueue(object:
                                    Callback<Void> {
                                    override fun onResponse(
                                        call: Call<Void>,
                                        response: Response<Void>
                                    ) {

                                        if (response.isSuccessful) {
                                            displayErrorMessage("record deleted", "Success")
                                            adapter.removeItem(position)
                                        }else{
                                            displayErrorMessage("failed to delete movie", "Failed")

                                        }
                                    }

                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        displayErrorMessage("failed to delete because "+ t.message, "Error")
                                    }

                                })

                            }
                        }


                    } else {
                        adapter.notifyItemChanged(position)
                    }

                }


            }
        }

    }

    private fun showDeleteConfirmationDialog(callback: (confirmed: Boolean) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                callback.invoke(true) // User confirmed deletion
            }
            .setNegativeButton("No") { _, _ ->
                callback.invoke(false) // User canceled deletion
            }
            .setOnCancelListener {
                callback.invoke(false) // User canceled deletion by dismissing the dialog
            }
            .show()
    }

    private fun displayErrorMessage(message: String, title: String) {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

}
