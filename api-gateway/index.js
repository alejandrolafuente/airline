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

// ------------------------------------  API COMPOSITION -------------------------------------


// R03
// app.get('/api-composition/combined-info/:id', validateTokenProxy, async (req, res) => {
app.get('/api-composition/combined-info/:id', async (req, res) => {

    try {
        const userId = req.params.id;

        // Fazendo as duas primeiras requisições em paralelo
        const [balanceResponse, clientBookingsResponse] = await Promise.all([
            // Requisição para pegar o balance do cliente
            axios.get(`http://localhost:8091/client/balance/${userId}`),
            // Requisição para pegar as reservas de voos
            axios.get(`http://localhost:8095/booking-query/client-bookings/${userId}`)
        ]);

        // Extraindo os dados
        const balance = balanceResponse.data;
        const clientBookings = clientBookingsResponse.data;

        console.log(balance);
        console.log(clientBookings);

        // Extraindo os códigos de voo das reservas
        const flightCodes = clientBookings.map(flight => flight.flightCode).join(',');

        // Fazendo a terceira requisição com os códigos de voo
        const flightDetailsResponse = await axios.get(`http://localhost:8093/flight/client-flights?flightCodes=${flightCodes}`);

        const flights = flightDetailsResponse.data;

        const completeBookings = [];

        for (let index = 0; index < flights.length; index++) {

            const matchingBooking = clientBookings.find(
                booking => booking.flightCode === flights[index].flightCode
            );

            const completeBooking = {
                flightDate: flights[index].flightDate,
                flighTime: flights[index].flighTime,
                departureAirport: flights[index].departureAirport,
                arrivalAirport: flights[index].arrivalAirport,
                flightCode: flights[index].flightCode,
                bookingId: matchingBooking ? matchingBooking.bookingId : null,
                bookingCode: matchingBooking ? matchingBooking.bookingCode : null,
                statusDescription: matchingBooking ? matchingBooking.statusDescription : null
            };

            completeBookings.push(completeBooking);

        }


        // Juntando os dados das três requisições
        const combinedData = {
            balance: balance.milesBalance,
            clientBookings: completeBookings
        };

        // Enviando a resposta final
        res.status(200).json(combinedData);

    } catch (error) {
        if (error.response && error.response.data) {
            // Caso a resposta contenha dados de erro customizados, os repassa ao front
            const { status, data } = error.response;
            console.error("Custom Error: ", data);
            res.status(status).send(data);
        } else {
            // Caso contrário, responde com um erro genérico
            console.error("Error: ", error.message || error);
            res.status(500).send({ message: 'Error fetching data', error: error.message || error });
        }
    }
});

// R04
app.get('/booking/:id', async (req, res) => {

    try {

        const bookingId = req.params.id;

        const [bookingResponse] = await Promise.all([
            // Requisição para pegar o balance do cliente
            axios.get(`http://localhost:8095/booking-query/booking/${bookingId}`),

        ]);

        // Extraindo os dados
        const booking = bookingResponse.data;

        // Extraindo o código de voo da reserva
        const flightCode = booking.flightCode;

        // Fazendo a terceira requisição com o código de voo
        const flightResponse = await axios.get(`http://localhost:8093/flight/booking-flight/${flightCode}`);

        console.log("Flight details response status: ", flightResponse.status);
        console.log("Flight details data: ", flightResponse.data);


        // Juntando os dados das duas requisições, tirar depois, serve para teste
        const combinedData = {
            booking: booking,
            flight: flightResponse.data
        };

        const flight = flightResponse.data;

        const bookingData = {
            bookingId: booking.bookingId,
            bookingDate: booking.bookingDate,
            bookingTime: booking.bookingTime,
            bookingCode: booking.bookingCode,
            departure: flight.departure,
            arrival: flight.arrival,
            moneySpent: booking.moneySpent,
            milesSpent: booking.milesSpent,
            seatsNumber: booking.seatsNumber,
            statusDescription: booking.statusDescription,
            flightCode: booking.flightCode
        };


        // Enviando a resposta final
        res.status(200).json(bookingData);

    } catch (error) {
        if (error.response && error.response.data) {
            // Caso a resposta contenha dados de erro customizados, os repassa ao front
            const { status, data } = error.response;
            console.error("Custom Error: ", data);
            res.status(status).send(data);
        } else {
            // Caso contrário, responde com um erro genérico
            console.error("Error: ", error.message || error);
            res.status(500).send({ message: 'Error fetching data', error: error.message || error });
        }
    }
});

// R06 
app.get('/miles-statement/:id', async (req, res) => {

    try {

        const userId = req.params.id;

        const [transactionsResponse] = await Promise.all([
            axios.get(`http://localhost:8091/client/miles-statement/${userId}`)
        ]);

        // Extraindo os dados
        const clientData = transactionsResponse.data;

        // Usando filter e map para selecionar e extrair transactionId
        const bookingTranIds = clientData.clientTransactions
            .filter(transaction => transaction.description === "FLIGHT BOOKING")
            .map(transaction => transaction.transactionId);

        // Fazendo a segunda requisição em booking query service com os ids das transacões?
        const bookingQueryResponse = await axios.get(`http://localhost:8095/booking-query/flight-codes?transactionIds=${bookingTranIds}`);

        flightCodes = bookingQueryResponse.data;

        // agora ir para o serviço de voos 
        const flightDetailsResponse = await axios.get(`http://localhost:8093/flight/client-flights?flightCodes=${flightCodes}`);

        console.log(flightDetailsResponse.data);

        res.status(200).json(clientData);

    } catch (error) {
        if (error.response && error.response.data) {
            // Caso a resposta contenha dados de erro customizados, os repassa ao front
            const { status, data } = error.response;
            console.error("Custom Error: ", data);
            res.status(status).send(data);
        } else {
            // Caso contrário, responde com um erro genérico
            console.error("Error: ", error.message || error);
            res.status(500).send({ message: 'Error fetching data', error: error.message || error });
        }
    }
});

// R09
app.get('/search-booking/:code', async (req, res) => {

    try {

        const bookingCode = req.params.code;

        const [bookingQuery] = await Promise.all([
            axios.get(`http://localhost:8095/booking-query/search-booking/${bookingCode}`)
        ]);

        const bookingData = bookingQuery.data;

        console.log(bookingData);

        res.status(200).json(bookingData);

    } catch (error) {
        if (error.response && error.response.data) {
            // Caso a resposta contenha dados de erro customizados, os repassa ao front
            const { status, data } = error.response;
            console.error("Custom Error: ", data);
            res.status(status).send(data);
        } else {
            // Caso contrário, responde com um erro genérico
            console.error("Error: ", error.message || error);
            res.status(500).send({ message: 'Error fetching data', error: error.message || error });
        }
    }


});

// *********************************************************************************
var server = http.createServer(app);
server.listen(3000);
console.log('Server running on port 3000');