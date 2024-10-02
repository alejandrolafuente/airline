import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LoginObject } from '../../models/loginObject/login-object.model';
import { LoginService } from '../../../authentication/services/login.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule, HttpClientModule],
  providers: [LoginService],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  title = 'AIRLINE';

  constructor(
    private router: Router,
    private loginService: LoginService
  ) { }

  get loggedUser(): LoginObject | null {
    return this.loginService.loggedUser;
    //return new LoginObject(undefined, "Chalito", "ale@emil.com", "ADMIN");
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  } 

}
