import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Login } from '../../../shared/models/login/login.model';
import { LoginObject } from '../../../shared/models/login-object/login-object.model';

const LS_LOGIN_KEY: string = "userLogged";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  BASE_URL = "http://localhost:3000";

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private httpClient: HttpClient) { }

  public get loggedUser(): LoginObject {
    let user = localStorage[LS_LOGIN_KEY];
    return (user ? JSON.parse(localStorage[LS_LOGIN_KEY]) : null);
  }

  public set loggedUser(user: LoginObject | undefined) {
    localStorage[LS_LOGIN_KEY] = JSON.stringify(user);
  }

  login(login: Login): Observable<LoginObject> {

    let loggedUser = this.httpClient.post<LoginObject>(this.BASE_URL + '/auth/login', JSON.stringify(login), this.httpOptions);
    return loggedUser;
  }

  logout() {
    let token = this.loggedUser.token;

    this.httpOptions.headers = this.httpOptions.headers.set('x-access-token', `${token}`);

    this.httpClient.post(this.BASE_URL + '/logout', this.httpOptions);

    delete localStorage[LS_LOGIN_KEY];

  }
}
