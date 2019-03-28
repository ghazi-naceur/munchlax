import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder } from '@angular/forms';

import { PersonService } from './person.service';
import { Person } from './person';

@Component({
    selector: 'app-person',
    templateUrl: './person.component.html',
    styleUrls: ['./person.component.css']
})

export class PersonComponent implements OnInit {

    persons: Person[];
    statusCode: number;
    requestProcessing = false;
    personIdToUpdate = null;
    processValidation = false;

    constructor(private personService: PersonService, private formBuilder: FormBuilder) {
    }

    personForm = new FormGroup({
        firstName: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        age: new FormControl('', Validators.required),
        occupation: new FormControl('', Validators.required)
    });

    searchPersonForm = this.formBuilder.group({
        firstName: ['', ],
        lastName: ['', ],
        age: ['', ],
        occupation: ['', ]
    });

    ngOnInit(): void {
        this.getAllPersons();
    }

    onPersonFormSubmit() {
        this.processValidation = true;

        if (this.personForm.invalid) {
            return;
        }

        this.preProcessConfigurations();
        let person = this.personForm.value;
        if (this.personIdToUpdate == null) {
            this.personService.createPerson(person)
                .subscribe(successCode => {
                    this.statusCode = successCode;
                    this.backToCreatePerson();
                    setTimeout(() => {
                        this.getAllPersons();
                    }, 500)
                },
                    errorCode => this.statusCode = errorCode
                );
        } else {
            person.id = this.personIdToUpdate;
            this.personService.updatePerson(person)
                .subscribe(successCode => {
                    this.statusCode = successCode;
                    this.getAllPersons();
                    this.backToCreatePerson();
                    setTimeout(() => {
                        this.getAllPersons();
                    }, 1000)
                },
                    errorCode => this.statusCode = errorCode);
        }


    }

    backToCreatePerson() {
        this.personForm.reset();
        this.processValidation = false;
        this.personIdToUpdate = null;
    }

    preProcessConfigurations() {
        this.statusCode = null;
        this.requestProcessing = true;
    }

    getAllPersons() {
        this.personService.getAllPersons()
            .subscribe(
                data => this.persons = data,
                errorCode => this.statusCode = errorCode);
    }

    loadPersonToEdit(personId: string) {
        this.preProcessConfigurations();
        this.personService.getPersonById(personId)
            .subscribe(person => {
                this.personIdToUpdate = person.id;
                this.personForm.setValue({
                    firstName: person.firstName,
                    lastName: person.lastName, age: person.age,
                    occupation: person.occupation
                });
                this.processValidation = true;
                this.requestProcessing = false;
            },
                errorCode => this.statusCode = errorCode);
    }

    deletePerson(personId: string) {
        this.preProcessConfigurations();
        this.personService.deletePersonById(personId)
            .subscribe(successCode => {
                this.statusCode = successCode;
                this.statusCode = 204;
                this.backToCreatePerson();

                setTimeout(() => {
                    this.getAllPersons();
                }, 500)
            },
                errorCode => this.statusCode = errorCode);
    }

    searchPersons(criteria) {
        this.personService.searchPersons(criteria)
            .subscribe(data => this.persons = data,
                errorCode => this.statusCode = errorCode);
    }

    onSearchPersonFormSubmit() {

        this.preProcessConfigurations();
        let searchCriteria = this.searchPersonForm.value;
        if (searchCriteria.firstName == "") {
            delete searchCriteria.firstName
        }
        if (searchCriteria.lastName == "") {
            delete searchCriteria.lastName
        }
        if (searchCriteria.age == "") {
            delete searchCriteria.age
        }
        if (searchCriteria.occupation == "") {
            delete searchCriteria.occupation
        }
        this.personService.searchPersons(searchCriteria)
            .subscribe(data => this.persons = data,
                errorCode => this.statusCode = errorCode);
        this.requestProcessing = false;
        this.searchPersonForm.reset();
    }

    refresh(){
        this.searchPersonForm.reset();
    }
}