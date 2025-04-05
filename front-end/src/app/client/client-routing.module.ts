import { Routes } from "@angular/router";
import { AuthGuard } from "../auth/services/auth.guard";
import { HomeComponent } from "./components/home/home.component";

export const ClientRoutes: Routes = [

    {
        path: 'client/home/:id',
        component: HomeComponent,
        canActivate: [AuthGuard],
        data: {
            role: 'CLIENT'
        }

    }
]