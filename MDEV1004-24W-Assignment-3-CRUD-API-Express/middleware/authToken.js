/*
 * Filename: authToken.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 14 March 2024
 */

const jwt = require("jsonwebtoken");
const routes = require("../routes/routes")


const verifyToken = (req,res,next) =>{
    const token = req.body.token || req.query.token || req.headers["x-access-token"];

    if(!token){
        return res.status(499).send("Authentication Token Required")
    }
    try{
        const decodeToken = jwt.verify(token, process.env.TOKEN_KEY)
        req.user = decodeToken;

    }
    catch(err){
        return res.status(401).send("Invalid Token");
    }
    return next();
}

module.exports = verifyToken;