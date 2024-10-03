import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-client-home-page',
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
  }

}
