import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { UserPromptDialogComponent } from './user-prompt-dialog.component';

import {configureTestingModule} from '../testing/testing-utils';

describe('UserPromptDialogComponent', () => {
  let component: UserPromptDialogComponent;
  let fixture: ComponentFixture<UserPromptDialogComponent>;

  beforeEach(async () => {
    await configureTestingModule({
      declarations: [ UserPromptDialogComponent ],
      imports: [ MatDialogModule ],
       providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPromptDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
