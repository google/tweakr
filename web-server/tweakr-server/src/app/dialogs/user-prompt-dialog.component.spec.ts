import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPromptDialogComponent } from './user-prompt-dialog.component';

describe('UserPromptDialogComponent', () => {
  let component: UserPromptDialogComponent;
  let fixture: ComponentFixture<UserPromptDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserPromptDialogComponent ]
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
