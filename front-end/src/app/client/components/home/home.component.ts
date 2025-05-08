import { Component, OnInit } from '@angular/core';
import { ClientLogin } from '../../../models/client-login/client-login.model';
import { ActivatedRoute } from '@angular/router';
import { ClientService } from '../../service/client.service';
import { ClientBooking } from '../../../models/client-booking/client-booking.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  clientId!: string; // comes from auth service
  clientLogin!: ClientLogin;
  balance: number | undefined;
  clientBookings: ClientBooking[] = [];

  constructor(
    private clientService: ClientService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.clientId = this.route.snapshot.params['id'];
    this.getClientData();
  }

  getClientData(): void {
    this.clientService.getClientData<ClientLogin>(this.clientId).subscribe({
      next: (clientData: ClientLogin) => {
        console.log(clientData);
        this.balance = clientData.balance;
        this.clientBookings = clientData.clientBookings;
      },
      error: (err) => {
        console.error('Error while getting client data', err);
      }
    })
  }

}
