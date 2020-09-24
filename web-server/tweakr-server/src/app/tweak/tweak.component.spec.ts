import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TweakComponent } from './tweak.component';

describe('TweakComponent', () => {
  let component: TweakComponent;
  let fixture: ComponentFixture<TweakComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TweakComponent ]
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
});
