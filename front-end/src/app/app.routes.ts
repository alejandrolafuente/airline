import { Routes } from '@angular/router';
import { LoginComponent } from './auth/components/login/login.component';
import { ClientRoutes } from './client/client-routing.module';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },

    {
        path: 'login',
        component: LoginComponent
    },

    ...ClientRoutes
];
