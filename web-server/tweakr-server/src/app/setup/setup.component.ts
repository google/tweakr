import { Component, OnInit } from '@angular/core';
import {Location} from "@angular/common";
import {Router} from '@angular/router';

import {AbstractControl, FormControl, FormGroupDirective, NgForm, Validators, ValidatorFn} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';

/**
 * Parses the first object from the given Javascript-like code. This is more
 * lenient than JSON.parse().
 */
function parseJsObject(js: string) {
  try {
    const start = js.indexOf('{');
    const end = js.lastIndexOf('}');
    const objJs = js.substring(start, end + 1);

    return (new Function(`return ${objJs};`))();
  } catch (e) {
    return null;
  }
}


/** Error when invalid control is dirty, touched, or submitted. */
export class JsonErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

function JsonValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    const isValid = parseJsObject(control.value);
    return isValid ? null : {json: true} ;
  };
}

@Component({
  selector: 'app-setup',
  templateUrl: './setup.component.html',
  styleUrls: ['./setup.component.scss']
})
export class SetupComponent implements OnInit {
  formControl = new FormControl('', [
    Validators.required,
    JsonValidator()
  ]);

  matcher = new JsonErrorStateMatcher();

  placeholderJs = `{
    apiKey: 'YOUR_INFO_HERE',
    authDomain: 'YOUR_INFO_HERE',
    databaseURL: 'YOUR_INFO_HERE',
    projectId: 'YOUR_INFO_HERE',
    storageBucket: 'YOUR_INFO_HERE',
    messagingSenderId: 'YOUR_INFO_HERE'
  }`;

  constructor(private router: Router, private location: Location) { }

  ngOnInit(): void {
  }

  onClicked() {
    //this.router.navigate(['tweak'], { queryParams: { config: configParam } });
    window.location.href = this.parseLink();
  }

  parseLink() {
    const configParam = encodeURIComponent(JSON.stringify(parseJsObject(this.formControl.value)));
    return this.location.prepareExternalUrl('/tweak?firebase=' + configParam);
  }
}
