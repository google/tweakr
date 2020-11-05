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

import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { TweakrComponent } from './tweakr.component';

import {AngularFireAuth} from '@angular/fire/auth';
import {AngularFireDatabase} from '@angular/fire/database';

import {AngularFireAuthMock, mockAngularFireDatabase} from './testing/angularfire-utils';

import {configureTestingModule} from './testing/testing-utils';

describe('TweakrComponent', () => {
  let component: TweakrComponent;
  let fixture: ComponentFixture<TweakrComponent>;

  beforeEach(async () => {
    await configureTestingModule({
      declarations: [ TweakrComponent ],
      providers: [
       { provide: AngularFireDatabase, useValue: mockAngularFireDatabase(null)},
       { provide: AngularFireAuth, useValue: AngularFireAuthMock}
     ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TweakrComponent);
    component = fixture.componentInstance;
    component.tweakrRoot = 'tweakr';
    component.userKey = 'userkey';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('no user key should show empty message', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('mat-card').textContent).toContain('No Tweaks found');
  });
});
