import { Routes } from "@angular/router";
import { ClientHomePageComponent } from "./components/client-home-page/client-home-page.component";
import { AuthGuard } from "../authentication/guard-service/auth.guard";

export const ClientRoutes: Routes = [

    {
        path: 'client/home/:id',
        component: ClientHomePageComponent,
        canActivate: [AuthGuard],
        data: {
            role: 'CLIENT'
        }

    }
]