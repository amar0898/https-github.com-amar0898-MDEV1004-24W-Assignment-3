/*
 * Filename: routes.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 13 March 2024
 */

require("dotenv").config();
const express = require("express")
const app = express();
app.use(express.json());
const auth = require("../middleware/authentication")
const bookController = require("../controller/controller");
// Create an instance of the Express Router
const router = express.Router();

// API Endpoints to perform the CRUD operations
// 1.API Endpoint to fetch all books 
router.get('/getAllBooks', bookController.getAllBooks);

// 2.API Endpoint to fetch one specific book using ID
router.get('/getBookByID/:id', bookController.getBookByID);

// 3.API Endpoint to create a new book  
router.post('/createBook', bookController.createBook);

// 4.API Endpoint to update the book by ID
router.put('/updateBook/:id', bookController.updateBook);

// 5.API Endpoint to delete a book using ID
router.delete('/deleteBookByID/:id', bookController.deleteBookByID);

module.exports = router;