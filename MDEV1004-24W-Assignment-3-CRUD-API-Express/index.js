/*
 * Filename: index.js
 * Amardeep Amardeep - 200567064
 * Yamuna Ravi Thalakatt - 200506480
 * Date: 12 March 2024
 */

require('dotenv').config();
const http = require("http");
const routes = require("./routes/routes")
const server = http.createServer(routes);

const { API_PORT } = process.env;
const port = process.env.PORT || API_PORT;

server.listen(port, ()=>{
    console.log(`server is running on port ${port}`)
})