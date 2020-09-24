import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TweakComponent } from './tweak.component';

const routes: Routes = [{ path: '', component: TweakComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TweakRoutingModule { }
