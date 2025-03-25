import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Login } from '../../models/login/login.model';
import { User } from '../../models/user/user.model';

const LS_LOGIN_KEY: string = "loggedUser";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  BASE_URL = "http://localhost:8080";

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private httpClient: HttpClient) { }

  public get loggedUser(): User {
    let user = localStorage[LS_LOGIN_KEY];
    return (user ? JSON.parse(localStorage[LS_LOGIN_KEY]) : null);
  }

  public set loggedUser(user: User) {
    localStorage[LS_LOGIN_KEY] = JSON.stringify(user);
  }

  login(login: Login): Observable<User> {

    let loggedUser = this.httpClient.post<User>(this.BASE_URL + '/auth/login', JSON.stringify(login), this.httpOptions);
    return loggedUser;
  }

  logout() {
    let token = this.loggedUser.token;

    this.httpOptions.headers = this.httpOptions.headers.set('x-access-token', `${token}`);

    this.httpClient.post(this.BASE_URL + '/logout', this.httpOptions);

    delete localStorage[LS_LOGIN_KEY];

  }

}
