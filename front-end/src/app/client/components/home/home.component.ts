import { Component, OnInit } from '@angular/core';
import { ClientLogin } from '../../../models/client-login/client-login.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  clientId!: String; // comes from auth service
  clientLogin!: ClientLogin;

  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.clientId = this.route.snapshot.params['id'];
  }

}
