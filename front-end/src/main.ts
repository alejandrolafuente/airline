import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';

// bootstrapApplication(AppComponent, appConfig)
//   .catch((err) => console.error(err));

bootstrapApplication(AppComponent, {
  ...appConfig, // Mantenha as configurações existentes
  providers: [
    ...(appConfig.providers || []), // Mantenha os providers existentes
    provideRouter(routes) // Adicione o roteador
  ]
}).catch((err) => console.error(err));