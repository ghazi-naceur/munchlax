import { Injectable } from '@angular/core';
// import { Http, Response, Headers, URLSearchParams, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
// import { map, filter, switchMap } from 'rxjs/operators';
import 'rxjs/add/operator/catch';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Http, Response, Headers, URLSearchParams, RequestOptions } from '@angular/http';

import { Person } from './person';

@Injectable()
export class PersonService {
    //URL for CRUD operations
    personUrl = "http://localhost:8080/persons";

    contentTypeHeader = new Headers({ 'Content-Type': 'application/json' });
    options = new RequestOptions({ headers: this.contentTypeHeader });
    //Create constructor to get Http instance
    constructor(private http: Http, private httpClient: HttpClient) {
    }

    // private options = { headers: new HttpHeaders().set('Content-Type', 'application/json') };

    // createPerson(employee: Person): Observable<Object> {
    //     let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    //     let options = { headers: headers };
    //     return this.httpClient.post(this.personUrl, employee, options);
    // }

    private handleError(error: Response | any) {
        console.error(error.message || error);
        return Observable.throw(error.status);
    }

    createPerson(person: Person): Observable<number> {
        return this.http.post(this.personUrl, person, this.options)
            .map(success => success.status)
            .catch(this.handleError);
    }

    getAllPersons(): Observable<Person[]> {
        return this.http.get(this.personUrl)
		   		.map(this.extractData)
		        .catch(this.handleError);
    }

    private extractData(res: Response) {
	    let body = res.json();
        return body;
    }

    updatePerson(person: Person):Observable<number> {
        return this.http.put(this.personUrl, person, this.options)
               .map(success => success.status)
               .catch(this.handleError);
    }

    getPersonById(personId: string): Observable<Person> {
		console.log(this.personUrl +"/"+ personId);
		return this.http.get(this.personUrl +"/"+ personId, this.options)
			   .map(this.extractData)
			   .catch(this.handleError);
    }

    deletePersonById(personId: string): Observable<number> {
		return this.http.delete(this.personUrl +"/"+ personId, this.options)
			   .map(success => success.status)
			   .catch(this.handleError);
    }
}
