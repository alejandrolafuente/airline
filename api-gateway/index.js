require("dotenv-safe").config();
const http = require('http');
const express = require('express');
const httpProxy = require('express-http-proxy');
const app = express();
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const helmet = require('helmet');
const cors = require('cors');
const axios = require('axios');

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

const authServiceProxy = httpProxy('http://localhost:8090');

const flightServiceProxy = httpProxy('http://localhost:8093');

const clientServiceProxy = httpProxy('http://localhost:8091');

const bookingQueryServiceProxy = httpProxy('http://localhost:8095');



// Middleware in order to verify token and forward to respective service
const validateTokenProxy = (req, res, next) => {
    const token = req.headers['x-access-token'];
    if (!token) {
        return res.status(401).send({ message: 'Token not provided!' });
    }

    const validationReqOptions = {
        headers: {
            'Content-Type': 'application/json',
            'x-access-token': token
        },
        method: 'GET'
    };

    const validationReq = http.request('http://localhost:8090/auth/validate', validationReqOptions, (validationRes) => {
        let data = '';
        validationRes.on('data', (chunk) => {
            data += chunk;
        });
        validationRes.on('end', () => {
            if (validationRes.statusCode === 200 && data === 'Token is valid') {
                next(); // valid token, proceed up to next middleware
            } else {
                res.status(401).send({ message: 'Token inválido!' });
            }
        });
    });

    validationReq.on('error', (err) => {
        res.status(500).send({ message: 'Validation token error', error: err.message });
    });

    validationReq.end();
};





//************************************** SYSTEM REQUIREMENTS ENDPOINTS ***************************


// ------------------ AUTHTENTICATION SERVICE

// R02 - login
app.post('/auth/login', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// Test method for Token validation
app.get('/auth/validate', (req, res, next) => {
    authServiceProxy(req, res, next);
});

// ------------------ CLIENT SERVICE

// R03
app.get('/client/balance/:id', (req, res, next) => {
    clientServiceProxy(req, res, next);
});



// ------------------- FLIGHT SERVICE

// este é um endoint de teste para que o usuario antes de acessar o serviço de
// listagem de aeroportos passe antes pelo serviço de autenticação e seu token seja
// autenticado. feita a autenticação o usuario é encaminhado para o serviço de voos
// e obtem a listagem dos aeroportos
app.get('/flight/airports/:id', validateTokenProxy, (req, res, next) => {
    flightServiceProxy(req, res, next);
});

// R03
app.get('/flight/bookedflights', (req, res, next) => {
    flightServiceProxy(req, res, next);
});

// ------------------- BOOKING QUERY SERVICE

// R03
app.get('/bookingquery/bookedflights/:id', (req, res, next) => {
    bookingQueryServiceProxy(req, res, next);
});

// ------------------- API COMPOSITION

// app.get('/api-composition/combined-info/:id', validateTokenProxy, async (req, res) => {


app.get('/api-composition/combined-info/:id', async (req, res) => {

    try {
        const userId = req.params.id;

        console.log("User ID: ", userId);

        // Fazendo as duas primeiras requisições em paralelo
        const [balanceResponse, bookedFlightsResponse] = await Promise.all([
            // Requisição para pegar o balance do cliente
            axios.get(`http://localhost:8091/client/balance/${userId}`),
            // Requisição para pegar as reservas de voos
            axios.get(`http://localhost:8095/bookingquery/bookedflights/${userId}`)
        ]);

        // Verificando os status das respostas
        console.log("Balance response status: ", balanceResponse.status);
        console.log("Booked flights response status: ", bookedFlightsResponse.status);

        // Extraindo os dados
        const balance = balanceResponse.data;
        const bookedFlights = bookedFlightsResponse.data;

        console.log("Balance data: ", balance);
        console.log("Booked flights data: ", bookedFlights);

        // Extraindo os códigos de voo das reservas
        const flightCodes = bookedFlights.map(flight => flight.flightCode).join(',');

        // Fazendo a terceira requisição com os códigos de voo
        const flightDetailsResponse = await axios.get(`http://localhost:8093/flight/bookedflights?flightCodes=${flightCodes}`);

        console.log("Flight details response status: ", flightDetailsResponse.status);
        console.log("Flight details data: ", flightDetailsResponse.data);


        // Juntando os dados das três requisições
        const combinedData = {
            balance: balance,
            bookedFlights: bookedFlights,
            flightDetails: flightDetailsResponse.data
        };

        // Enviando a resposta final
        res.status(200).json(combinedData);

    } catch (error) {
        console.error("Error: ", error.message || error);
        res.status(500).send({ message: 'Error fetching data', error: error.message || error });
    }
});


// *********************************************************************************
var server = http.createServer(app);
server.listen(3000);
console.log('Server running on port 3000');