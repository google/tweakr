import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {AngularFireModule} from '@angular/fire';
import {AngularFireAuthModule} from '@angular/fire/auth';
import {AngularFireDatabaseModule} from '@angular/fire/database';

import { TweakRoutingModule } from './tweak-routing.module';
import { TweakComponent } from './tweak.component';

import {EditorsModule} from '../editors/editors.module';
import {TweakrComponent} from '../tweakr.component';

import {environment} from '../../environments/environment';


@NgModule({
  declarations: [TweakComponent, TweakrComponent],
  imports: [
    CommonModule,
    TweakRoutingModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireAuthModule, AngularFireDatabaseModule,
    EditorsModule
  ]
})
export class TweakModule { }
