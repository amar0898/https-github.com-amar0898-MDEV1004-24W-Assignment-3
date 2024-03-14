/*
 * Filename: routes.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 13 March 2024
 */

function logGetAllBooks(req, res, next) {

    console.log('Fetching all Books');
      next();
    }
     
const logGetBookByID = (req, res, next) => {
        console.log(`Fetching Books By Id: ${req.params.id}`);
        next();
      };
     
const logCreateNewBook = (req, res, next) => {
        if (req.body && req.body.BookName) { // Check if req.body is defined and contains BookName
          console.log('Book Name:', req.body.BookName);
      } else {
          console.error('BookName property is missing or undefined in request body');
      }
        next();
      };
     
     
 const logUpdateBookByID = (req, res, next) => {
        console.log(`Updating Book with ID: ${req.params.id}, new title: ${req.body.BookName}`);
        next();
      };
     
 const logDeleteBook = (req, res, next) => {
        console.log(`Deleting Book with ID: ${req.params.id}`);
        next();
      };

function logRegisterUser(req, res, next) {

        console.log('Creating account for user');
          next();
        }

function logLoginUser(req, res, next) {

            console.log('Logging in the user');
              next();
            }

function logWelcomeUser(req, res, next) {

                console.log('Welcome user');
                  next();
                }
     
      module.exports = {
        logGetAllBooks,
        logGetBookByID,
        logCreateNewBook,
        logUpdateBookByID,
        logDeleteBook,
        logRegisterUser,
        logLoginUser,
        logWelcomeUser
      };