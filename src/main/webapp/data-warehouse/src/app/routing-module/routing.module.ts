
import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from '../components/not-found/not-found.component';
import { WelcomeComponent } from '../components/welcome/welcome.component';
import { PersonsListComponent } from "../components/persons-list/persons-list.component";

const routes: Routes = [
  {path: 'welcome', component: WelcomeComponent},
  {path: 'list', component: PersonsListComponent},
  {path: '', component: WelcomeComponent, pathMatch: 'full'},
  {path: '**', component: NotFoundComponent}
]; 

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
  providers: [],
  bootstrap: []
})
export class RoutingModule { }
