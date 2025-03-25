import { Component, ViewChild } from '@angular/core';
import { NgForm, FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Login } from '../../../models/login/login.model';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterLink,
    RouterModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  @ViewChild('formLogin')
  formLogin!: NgForm;

  login: Login = new Login();
  loading: boolean = false;
  message!: string;

  constructor(
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // if (this.loginService.loggedUser) {
    //   this.router.navigate(["/accounts"]);
    // }
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => { this.message = params['error'] });
  }

  doLogin(): void {

    this.loading = true;

    if (this.formLogin.form.valid) {
      let observable = this.loginService.login(this.login);

      observable.subscribe(

        (user) => {
          if (user != null) {
            this.loginService.loggedUser = user; //*** setting LoginObject in LS
            this.loading = false;
            if (user.role == "CLIENT") {
              this.router.navigate(["/client/home/" + user.userId]);
            }
            // else if (user.role == "CLIENT") {//goes to account-read service
            //   this.router.navigate(["/client/" + user.userId]);
            // }
            // calls auth.guard.ts when activating route
          }                                     // replace by /home later
          else {
            this.message = "invalid user/password";
          }
        });
    }
    else { this.loading = false; }
  }
}
