import { Injectable } from '@angular/core';
import { Person } from '../domain/person';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';


@Injectable()
export class PersonService {

  url = 'http://localhost:8080/persons/';

  constructor(private http: HttpClient) { }

  public getPerson(): Observable<Person[]> {
    return this.http.get<Person[]>(this.url);
  }
}
