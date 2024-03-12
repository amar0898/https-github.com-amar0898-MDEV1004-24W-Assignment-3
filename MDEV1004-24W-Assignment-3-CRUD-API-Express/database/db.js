/*
 * Filename: db.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 12 March 2024
 */

require('dotenv').config();
const mongoose = require("mongoose")
const { MONGO_URI } = process.env;

exports.connect=()=>(
    mongoose.connect(MONGO_URI, {
    })
    .then(()=>{
        console.log("Connected to mongoDB database successfully")
    })
    .catch((error)=>{
        console.log("Conection failed")
        console.log(error)
    })
)