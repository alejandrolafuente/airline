import { ClientBooking } from "../client-booking/client-booking.model";

export class ClientLogin {
    constructor(
        public balance: number,
        public clientBookings: ClientBooking[]
    ) { }
}
