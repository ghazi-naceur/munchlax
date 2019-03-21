import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PersonComponent } from './persons/person.component';
import { PersonService } from './persons/person.service';
import { HttpModule } from '@angular/http';
import { MatTableModule } from '@angular/material';
import { DataFileComponent } from './datafile/data.file.component';
import { DataFileService } from './datafile/data.file.service';

@NgModule({
  declarations: [
    AppComponent,
    PersonComponent,
    DataFileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
		HttpClientModule,
    ReactiveFormsModule,
    HttpModule,
    MatTableModule,
    FormsModule
  ],
  providers: [
    PersonService,
    DataFileService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
