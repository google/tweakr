import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';

/** Error when invalid control is dirty, touched, or submitted. */
export class JsonErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-setup',
  templateUrl: './setup.component.html',
  styleUrls: ['./setup.component.scss']
})
export class SetupComponent implements OnInit {
  formControl = new FormControl('', [
    Validators.required
  ]);

  matcher = new JsonErrorStateMatcher();

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  onClicked() {
    const configParam = encodeURIComponent(this.formControl.value);
    //this.router.navigate(['tweak'], { queryParams: { config: configParam } });
    window.location.href = '/tweak?firebase=' + configParam;
  }
}
