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

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: 'tweak', loadChildren: () => import('./tweak/tweak.module').then(m => m.TweakModule) },
  { path: 'setup', loadChildren: () => import('./setup/setup.module').then(m => m.SetupModule) }
];

@NgModule({
  // Note there are two good reasons for using HashLocationStrategy here:
  //   1. So the easyserver works on Github pages.
  //   2. The location hash is never sent to the server, so it can't log your API keys.
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
