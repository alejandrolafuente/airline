//import { CanActivateFn } from '@angular/router';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

// export const authGuard: CanActivateFn = (route, state) => {
//   return true;
// };
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private loginService: LoginService,
    private router: Router
  ) { }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


    const loggedInUser = this.loginService.loggedUser; // getter

    let url = state.url;

    if (loggedInUser) { // if  user is logged in local storage...

      if (route.data?.['role'] && route.data?.['role'].indexOf(loggedInUser.role) === -1) {
        // If the user profile is not in the route profile
        // goes to login
        this.router.navigate(['/login'],
          { queryParams: { error: "Forbiden access to " + url } });
        return false;
      }
      // in any other case allow access
      return true;
    }
    // if not logged goes to login
    this.router.navigate(['/login'],
      { queryParams: { error: "Must be looged in before access " + url } });
    return false;
  }
}

