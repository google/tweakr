import { HarnessLoader } from "@angular/cdk/testing";
import { TestbedHarnessEnvironment } from "@angular/cdk/testing/testbed";
import { TestBed, async, ComponentFixture } from "@angular/core/testing";
import { MatFormFieldHarness } from "@angular/material/form-field/testing";
import { MatInputHarness } from "@angular/material/input/testing";

import { SetupComponent } from './setup.component';
import {configureTestingModule} from '../testing/testing-utils';

let loader: HarnessLoader;

describe('SetupComponent', () => {
  let component: SetupComponent;
  let fixture: ComponentFixture<SetupComponent>;

  beforeEach(async () => {
    await configureTestingModule({
      declarations: [ SetupComponent ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SetupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.ngOnInit();
    fixture.detectChanges();

    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render title in the nav', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('mat-toolbar').textContent).toContain('Tweakr');
  });

  it('entering invalid API config should show error', async () => {
    const textarea = await loader.getHarness(MatInputHarness);
    const form = await loader.getHarness(MatFormFieldHarness);

    expect(textarea).toBeTruthy();
    expect(form).toBeTruthy();

    await textarea.setValue(`apiKey: 'YOUR_INFO_HERE',`);
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('mat-error').textContent).toContain('Invalid');

    expect((await form.getTextErrors())[0]).toContain('Invalid');
  });

  it('entering API config should render link', async () => {
    const textarea = await loader.getHarness(MatInputHarness);
    const form = await loader.getHarness(MatFormFieldHarness);

    expect(textarea).toBeTruthy();
    expect(form).toBeTruthy();

    await textarea.setValue(`{
      apiKey: 'YOUR_INFO_HERE',
      authDomain: 'YOUR_INFO_HERE',
      databaseURL: 'YOUR_INFO_HERE',
      projectId: 'YOUR_INFO_HERE',
      storageBucket: 'YOUR_INFO_HERE',
      messagingSenderId: 'YOUR_INFO_HERE'
    }`);
    fixture.detectChanges();

    expect((await form.getTextErrors()).length).toBe(0);

    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('a').href).toContain('YOUR_INFO_HERE');
    expect(compiled.querySelector('a').href).toContain('databaseURL');
  });
});
