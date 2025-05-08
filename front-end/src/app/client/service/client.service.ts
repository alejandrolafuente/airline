import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginService } from '../../auth/services/login.service';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(
    private httpClient: HttpClient,
    private loginService: LoginService
  ) { }

  BASE_URL = "http://localhost:3000";

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  setToken() {
    let token = this.loginService.loggedUser.token;
    this.httpOptions.headers = this.httpOptions.headers.set('x-access-token', `${token}`);
  }

  getClientData<T>(id: string): Observable<T> {
    this.setToken();
    return this.httpClient.get<T>(this.BASE_URL + '/client/home/' + id, this.httpOptions);
  }
}
