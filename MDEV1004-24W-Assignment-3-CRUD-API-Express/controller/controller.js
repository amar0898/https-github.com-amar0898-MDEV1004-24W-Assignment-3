/*
 * Filename: routes.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 13 March 2024
 */

const { BookModel, syncModelWithCloud } = require('../models/bookModel'); // Importing the BookModel and syncModelWithCloud from the bookModel module

// 1. function to fetch all books 
exports.getAllBooks = async (req, res) => {
  try {
    const books = await BookModel.find();
    
    //display the result into tabular format
    res.json(books)

  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// 2. Function to fetch one specific book using ID
exports.getBookByID = async (req, res) => {
  const { id } = req.params;
  try {
    const book = await BookModel.findById(id);
    res.json(book);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};


// 3. Function to create a new book  
exports.createBook = async (req, res) => {
  const bookData = req.body;
  try {
    const newBook = await BookModel.create(bookData);
    res.status(201).json(newBook);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// 4. Function to update the book by ID
exports.updateBook = async (req, res) => {
  const { id } = req.params;
  const updatedBookData = req.body;
  try {
    const updatedBook = await BookModel.findByIdAndUpdate(id, updatedBookData, { new: true });
    res.json(updatedBook);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// 5. Function to delete a book using ID
exports.deleteBookByID = async (req, res) => {
  const { id } = req.params;
  try {
    await BookModel.findByIdAndDelete(id);
    res.json({ message: 'book deleted successfully' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};