import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { HttpClientModule } from '@angular/common/http';

// import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { RoutingModule } from './routing-module/routing.module';
import { PersonsListComponent } from "./components/persons-list/persons-list.component";
import { PersonService } from "./services/person.service";

// const routes: Routes = [
//   {path: 'welcome', component: WelcomeComponent},
//   {path: 'list', component: ProductListComponent},
//   {path: '', component: WelcomeComponent, pathMatch: 'full'},
//   {path: '**', component: NotFoundComponent}
// ]; // Routes table

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    PersonsListComponent,
    FooterComponent,
    WelcomeComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    // RouterModule.forRoot(routes),
    RoutingModule
  ],
  providers: [PersonService],
  bootstrap: [AppComponent]
})
export class AppModule { }
