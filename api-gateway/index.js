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


// Middleware para validar o token
function validateToken(req, res, next) {
    const token = req.headers['x-access-token'];

    if (!token) {
        return res.status(403).send({ message: 'No token provided.' });
    }

    // Realiza uma requisição HTTP ao serviço de autenticação para validar o token
    const authServiceProxy = httpProxy('http://localhost:8082');

    // Envia a requisição de validação de token
    const validateReq = http.request({
        hostname: 'localhost',
        port: 8082,
        path: '/auth/validate',
        method: 'GET',
        headers: { 'x-access-token': token }
    }, (authRes) => {
        let data = '';

        // Coleta os dados de resposta do serviço de autenticação
        authRes.on('data', (chunk) => {
            data += chunk;
        });

        authRes.on('end', () => {
            if (authRes.statusCode === 200) {
                // Se o token for válido, continua para o próximo middleware (o serviço de voos)
                next();
            } else {
                // Se a validação falhar, responde com o erro apropriado
                res.status(authRes.statusCode).send({ message: 'Invalid token' });
            }
        });
    });

    validateReq.on('error', (err) => {
        res.status(500).send({ message: 'Error validating token' });
    });

    validateReq.end();
}





//************************************** SYSTEM REQUIREMENTS ENDPOINTS ***************************


// AUTHTENTICATION SERVICE

// R2 - login
app.post('/auth/login', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// Test method for Token validation
app.get('/auth/validate', (req, res, next) => {
    authServiceProxy(req, res, next);
});


// FLIGHT SERVICE

app.get('/flight/airports', validateToken, (req, res, next) => {
    flightServiceProxy(req, res, next);
});


// ***********************************************************************************************
var server = http.createServer(app);
server.listen(3000);
console.log('Server running on port 3000');