package com.example.assignment3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private var books: List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookAdapter.ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        val movie = books[position]
        holder.bind(movie)
        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
            onItemClickListener?.onItemClick(movie)
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(model: Book)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return books.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = book.bookName
            itemView.findViewById<TextView>(R.id.AuthorTextView).text = book.author
            var rating = itemView.findViewById<TextView>(R.id.ratingTextView)
            rating.text = book.rating.toString()


            if (book.rating > 3.5) {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.green, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else if (book.rating > 2) {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.yellow, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.red, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }
            // Bind other views as needed
        }

    }

    fun getItem(position: Int): Book {
        return books[position]
    }

    fun removeItem(position: Int) {
        books = books.minusElement(books[position])
    }
}
