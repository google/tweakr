// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatToolbarModule} from '@angular/material/toolbar';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import { ConfirmationDialogComponent } from './dialogs/confirmation-dialog.component';
import { UserPromptDialogComponent } from './dialogs/user-prompt-dialog.component';

@NgModule({
  declarations: [AppComponent, ConfirmationDialogComponent, UserPromptDialogComponent],
  imports: [
    BrowserModule, BrowserAnimationsModule, AppRoutingModule,
    FormsModule, MatButtonModule, MatCardModule, MatToolbarModule,
    MatDialogModule, MatInputModule, ReactiveFormsModule
  ],
  entryComponents: [
    ConfirmationDialogComponent,
    UserPromptDialogComponent
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [ConfirmationDialogComponent, UserPromptDialogComponent]
})
export class AppModule {
}
