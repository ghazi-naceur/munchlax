import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PersonComponent } from './persons/person.component';
import { DataFileComponent } from './datafile/data.file.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  {path: 'person', component: PersonComponent},
  {path: 'datafile', component: DataFileComponent},
  {path: '**', component: NotFoundComponent}
]; // Routes table

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
