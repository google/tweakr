import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { TweakComponent } from './tweak.component';

describe('TweakComponent', () => {
  let component: TweakComponent;
  let fixture: ComponentFixture<TweakComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TweakComponent ],
      imports: [ MatDialogModule, RouterTestingModule ],
      providers: [
       { provide: MAT_DIALOG_DATA, useValue: {} }
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
