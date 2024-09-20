import { Routes } from "@angular/router";
import { LoginComponent } from "./login/login.component";

export const LoginRoutes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent
    }
];