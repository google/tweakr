import { Component, Inject, OnInit } from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

import {AbstractControl, FormControl, FormGroupDirective, NgForm, Validators, ValidatorFn} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';


/** Error when invalid control is dirty, touched, or submitted. */
export class CodeErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

interface ValidCodeMatcher {
  isCodeValid(code: string): boolean;
}

function CodeValidator(validCodeMatcher: ValidCodeMatcher): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    const isValid = validCodeMatcher.isCodeValid(control.value);
    return isValid ? null : {validCode: true} ;
  };
}

@Component({
  selector: 'app-user-prompt-dialog',
  templateUrl: './user-prompt-dialog.component.html',
  styleUrls: ['./user-prompt-dialog.component.scss']
})
export class UserPromptDialogComponent implements OnInit, ValidCodeMatcher {

  formControl = new FormControl('', [
    Validators.required,
    CodeValidator(this)
  ]);

  matcher = new CodeErrorStateMatcher();

  getMessage() {
    return this.data?.message || 'Enter User PIN code';
  }

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
  }

  isCodeValid(code: string): boolean {
    return this.data?.validCodes?.includes(code);
  }
}
