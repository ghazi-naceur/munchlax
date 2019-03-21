import { Component, OnInit } from '@angular/core';
import { DataFileService } from './data.file.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';


@Component({
  selector: 'data-file',
  templateUrl: './data.file.component.html',
  //   styleUrls: ['./data.file.component.css']
})
export class DataFileComponent implements OnInit {

  providedInput: string;
  statusCode: number;

  dataFilesForm = new FormGroup({
    path: new FormControl('', Validators.required)
  });

  constructor(private dataFileService: DataFileService) {
  }

  ngOnInit(): void {

  }

  sendPath(path) {
    let dataFiles = this.dataFilesForm.value;
    this.dataFileService.sendPath(dataFiles).subscribe(successCode => {
      this.statusCode = successCode;
      this.providedInput = null;
    },
      errorCode => this.statusCode = errorCode
    );
  }

  isInvalid() {
    return this.providedInput == undefined
  }
}