/*
 * Filename: db.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 12 March 2024
 */

require('dotenv').config();
const mongoDB = require("mongoose")
const { MONGO_URI } = process.env;

mongoDB.connect(MONGO_URI);
const db = mongoDB.connection;

db.on('error', console.error.bind(console, 'MongoDB connection error:'));
db.once('open', () => {
  console.log('Successfully connected to MongoDB database');
});

module.exports = { mongoDB, db };