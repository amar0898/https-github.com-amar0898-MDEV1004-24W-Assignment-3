/*
 * Filename: bookModel.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 13 March 2024
 */

const mongoose = require("mongoose");
const userSchema = new mongoose.Schema({
    firstname:{
        type:String,
        default:null
    },
    lastname:{
        type:String,
        default:null
    },
    email:{
        type:String,
        unique:true
    },
    password:{
        type:String,
        default:null
    },
    token:{type:String}

});

// Defining the User model using the userSchema created above
const UserModel = mongoose.model('User', userSchema);

module.exports = { UserModel };