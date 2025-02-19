import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginObject } from './shared/models/login-object/login-object.model';
import { LoginService } from './authentication/services/login/login.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  title = 'AIRLINE';

  constructor(
    private router: Router,
    private loginService: LoginService
  ) { }

  get loggedUser(): LoginObject | null {
    return this.loginService.loggedUser;

  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

}

