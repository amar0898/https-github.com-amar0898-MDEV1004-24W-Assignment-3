/*
 * Filename: bookModel.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 13 March 2024
 */

// Importing the Mongoose library to establish the connect with MongoDB database
const mongoose = require('mongoose');

const bookSchema = new mongoose.Schema({
  BooksName: { 
    type: String, 
    required: true, 
},
  ISBN: { 
    type: String, 
    unique: true,
    required: true,  
},
  Rating: { 
    type: Number, 
    required: true, 
},
  Author: { 
    type: String, 
    required: true, 
},
  Genre: { 
    type: String, 
    required: true, 
},
created_date:{
    type:Date,
    default: Date.now
}
}, { bufferCommands: false });

// Defining the Book model using the bookSchema created above
const BookModel = mongoose.model('Book', bookSchema);

// Sync Model with Cloud
async function syncModelWithCloud() {

    // Check if data already exists in database to avoid duplicate inserts
    const existingData = BookModel.find(); //await
  
    if (existingData.length === undefined) {
      // Insert example data only if no data exists
      await BookModel.create([
        { BooksName: 'The Hobbit', ISBN: '1112223334', Rating: 4.7, Author: 'J.R.R. Tolkien', Genre: 'Fantasy' },
        { BooksName: 'To Kill a Mockingbird', ISBN: '9780061120084', Rating: 4.27, Author: 'Harper Lee', Genre: 'Classics' },
  
        { BooksName: '1984', ISBN: '9780452284234', Rating: 4.18, Author: 'George Orwell', Genre: 'Classics' },
  
        { BooksName: 'The Great Gatsby', ISBN: '9780743273565', Rating: 3.93, Author: 'F. Scott Fitzgerald', Genre: 'Classics' },
  
        { BooksName: 'Pride and Prejudice', ISBN: '9780141439518', Rating: 4.28, Author: 'Jane Austen', Genre: 'Classics' },
        { BooksName: 'The Catcher in the Rye', ISBN: '9780316769488', Rating: 3.81, Author: 'J.D. Salinger', Genre: 'Classics' },
  
        { BooksName: 'Harry Potter and the Sorcerer\'s Stone', ISBN: '9780590353427', Rating: 4.48, Author: 'J.K. Rowling', Genre: 'Fantasy' },
  
        { BooksName: 'The Lord of the Rings', ISBN: '9780618640157', Rating: 4.51, Author: 'J.R.R. Tolkien', Genre: 'Fantasy' },
  
        { BooksName: 'The Chronicles of Narnia', ISBN: '9780066238500', Rating: 4.26, Author: 'C.S. Lewis', Genre: 'Fantasy' },
  
        { BooksName: 'The Da Vinci Code', ISBN: '9780307474278', Rating: 3.85, Author: 'Dan Brown', Genre: 'Thriller' },
  
        { BooksName: 'The Hunger Games', ISBN: '9780439023481', Rating: 4.33, Author: 'Suzanne Collins', Genre: 'Young Adult' },
  
        { BooksName: 'The Alchemist', ISBN: '9780061122415', Rating: 3.86, Author: 'Paulo Coelho', Genre: 'Fiction' },
  
        { BooksName: 'The Shining', ISBN: '9780307743657', Rating: 4.2, Author: 'Stephen King', Genre: 'Horror' },
  
        { BooksName: 'Moby-Dick', ISBN: '9780142437247', Rating: 3.49, Author: 'Herman Melville', Genre: 'Classics' },
  
        { BooksName: 'Brave New World', ISBN: '9780060850524', Rating: 3.98, Author: 'Aldous Huxley', Genre: 'Classics' },
  
        { BooksName: 'Gone with the Wind', ISBN: '9780446675536', Rating: 4.3, Author: 'Margaret Mitchell', Genre: 'Historical Fiction' },
  
        { BooksName: 'The Hitchhiker\'s Guide to the Galaxy', ISBN: '9780345391803', Rating: 4.22, Author: 'Douglas Adams', Genre: 'Science Fiction' },
  
        { BooksName: 'Wuthering Heights', ISBN: '9780141439556', Rating: 3.85, Author: 'Emily Brontë', Genre: 'Classics' },
  
        { BooksName: 'The Picture of Dorian Gray', ISBN: '9780486278070', Rating: 4.07, Author: 'Oscar Wilde', Genre: 'Classics' },
  
        { BooksName: 'Frankenstein', ISBN: '9780486282114', Rating: 3.78, Author: 'Mary Shelley', Genre: 'Classics' }
  
      ]);
      console.log('Favourite Movie data inserted');
    } else {
      console.log('Favourite Movie data already exists. Skipping insertion.');
    }
  }

module.exports = { BookModel, syncModelWithCloud };