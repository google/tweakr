import { RouterTestingModule } from '@angular/router/testing';

import { TestBed, async, ComponentFixture, TestModuleMetadata, TestBedStatic } from "@angular/core/testing";
import { MatFormFieldModule } from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { MatSelectModule } from "@angular/material/select";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';

/**
 * Use this instead of `TestBed.configureTestingModule()`: it will automatically
 * add the common modules that all the components need.
 */
export function configureTestingModule(moduleDef: Partial<TestModuleMetadata>): TestBedStatic {
  const imports = moduleDef.imports ? moduleDef.imports : [];
  const providers = moduleDef.providers ? moduleDef.providers : [];
  return TestBed.configureTestingModule({
    ...moduleDef,
    // declarations: moduleDef.declarations,
    imports: [
      MatDialogModule,
      RouterTestingModule,
      FormsModule,
      ReactiveFormsModule,
      MatFormFieldModule,
      MatSelectModule,
      MatCardModule,
      MatButtonModule,
      MatInputModule,
      NoopAnimationsModule,
      ...imports],
    providers: [
      { provide: MAT_DIALOG_DATA, useValue: {} },
      ...providers
    ]
  });
}
