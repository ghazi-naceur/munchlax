import { Component, OnInit } from '@angular/core';
import { Person } from '../../domain/person';
import { PersonService } from '../../services/person.service';

@Component({
  selector: 'app-persons-list',
  templateUrl: './persons-list.component.html',
  styleUrls: ['./persons-list.component.css']
})
export class PersonsListComponent implements OnInit {

  public data: Person[];

  constructor(private _service: PersonService) { // Déclarer une dépendance sur le service
  }

  ngOnInit() { // équivalent à @PostConstruct en Spring
    this._service.getPerson().subscribe(
      result => { this.data = result; }
    );
  }

}
