import { Routes } from "@angular/router";
import { ClientHomePageComponent } from "./components/client-home-page/client-home-page.component";

export const ClientRoutes: Routes = [
    {
        path: 'client/home/:id',
        component: ClientHomePageComponent
    }
];