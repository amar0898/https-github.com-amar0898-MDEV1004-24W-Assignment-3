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
const middleware = require("../middleware/middleware")
const auth = require("../middleware/authToken")
const bookController = require("../controller/controller");
const bcrypt = require("bcryptjs")
const jwt = require("jsonwebtoken")
const User = require("../models/userModel");
// Create an instance of the Express Router
const router = express.Router();

// API Endpoints to perform the CRUD operations
// 1.API Endpoint to fetch all books 
router.get('/getAllBooks', bookController.getAllBooks, auth);

// 2.API Endpoint to fetch one specific book using ID
router.get('/getBookByID/:id', bookController.getBookByID, auth);

// 3.API Endpoint to create a new book  
router.post('/createBook', bookController.createBook, auth);

// 4.API Endpoint to update the book by ID
router.put('/updateBook/:id', bookController.updateBook, auth);

// 5.API Endpoint to delete a book using ID
router.delete('/deleteBookByID/:id', bookController.deleteBookByID, auth);

//register
router.post("/register", bookController.registerUser, auth);

//login
router.post("/login", bookController.loginUser, auth);

//welcome
router.post("/welcome", bookController.welcomeUser, auth);

module.exports = router;
