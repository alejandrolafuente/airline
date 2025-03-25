import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Login } from '../../models/login/login.model';
import { User } from '../../models/user/user.model';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor() { }
}
