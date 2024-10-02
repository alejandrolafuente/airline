import { Routes } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { ClientRoutes } from './client/client-routing.module';

export const routes: Routes = [
    {
        path: '',
        component: LoginComponent
    },

    ...ClientRoutes
];
