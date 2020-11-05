import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';

import {configureTestingModule} from '../testing/testing-utils';

import {AngularFireAuth} from '@angular/fire/auth';
import {AngularFireDatabase} from '@angular/fire/database';

import { TweakComponent } from './tweak.component';
import {AngularFireAuthMock, mockAngularFireDatabase} from '../testing/angularfire-utils';

describe('TweakComponent', () => {
  let component: TweakComponent;
  let fixture: ComponentFixture<TweakComponent>;

  beforeEach(async () => {
    await configureTestingModule({
      declarations: [ TweakComponent ],
      imports: [ MatDialogModule, RouterTestingModule ],
      providers: [
       { provide: MAT_DIALOG_DATA, useValue: {} },
       { provide: AngularFireDatabase, useValue: mockAngularFireDatabase(null)},
       { provide: AngularFireAuth, useValue: AngularFireAuthMock}
     ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TweakComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render title in the nav', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('mat-toolbar').textContent).toContain('Tweakr');
  });
});
