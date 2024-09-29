require("dotenv-safe").config();
const http = require('http');
const express = require('express');
const httpProxy = require('express-http-proxy');
const app = express();
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const helmet = require('helmet');
const cors = require('cors');

// Cors configuration, must be changed to ramdom port with .sh automatization script
const corsOptions = {
    origin: ['http://localhost:4200'],
    allowedHeaders: ['Content-Type', 'x-access-token'],
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS']
};

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(logger('dev'));
app.use(helmet());
app.use(cors(corsOptions));

app.options('*', cors(corsOptions));


// PROXIES

const authServiceProxy = httpProxy('http://localhost:8082');

const flightServiceProxy = httpProxy('http://localhost:8084');


//************************************** SYSTEM REQUIREMENTS ENDPOINTS ***************************


// R2 - login
app.post('/auth/login', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// Test method for Token validation
app.get('/auth/validate', (req, res, next) => {
    authServiceProxy(req, res, next);
});


// ***********************************************************************************************
var server = http.createServer(app);
server.listen(3000);
console.log('Server running on port 3000');