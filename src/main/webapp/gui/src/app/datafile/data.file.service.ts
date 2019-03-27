import { Injectable } from '@angular/core';
// import { Http, Response, Headers, URLSearchParams, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
// import { map, filter, switchMap } from 'rxjs/operators';
import 'rxjs/add/operator/catch';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Http, Response, Headers, URLSearchParams, RequestOptions } from '@angular/http';
import { DataFiles } from './DataFiles';


@Injectable()
export class DataFileService {

    datafilesUrl = 'http://localhost:8080/datafile';

    contentTypeHeader = new Headers({ 'Content-Type': 'application/json' });
    options = new RequestOptions({ headers: this.contentTypeHeader });

    constructor(private http: Http){
    }

    sendPath(dataFiles: DataFiles): Observable<string[]> {
        return this.http.post(this.datafilesUrl, dataFiles, this.options)
            .map(this.extractData)
            .catch(this.handleError);
    }
    
    private handleError(error: Response | any) {
        console.error(error.message || error);
        return Observable.throw(error.status);
    }

    private extractData(res: Response) {
	    let body = res.json();
        return body;
    }
}