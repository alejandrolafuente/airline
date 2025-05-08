export class ClientBooking {
    constructor(
        public flightDate?: string,
        public flighTime?: string,
        public departureAirport?: string,
        public arrivalAirport?: string,
        public flightCode?: string,
        public bookingId?: string,
        public bookingCode?: string,
        public statusDescription?: string
    ) { }
}
