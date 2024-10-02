import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';


@Component({
  selector: 'app-client-home-page',
  standalone: true,
  imports: [RouterModule, CommonModule, HttpClientModule],
  templateUrl: './client-home-page.component.html',
  styleUrl: './client-home-page.component.css'
})
export class ClientHomePageComponent implements OnInit {

  clientUserId!: string;

  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.clientUserId = this.route.snapshot.params['id'];
    this.listAccountRequests();
  }

  listAccountRequests(): void {}

}
