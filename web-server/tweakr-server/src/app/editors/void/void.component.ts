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

import { Component } from '@angular/core';
import { EditorComponent } from '../editor/editor.component';

@Component({
  selector: 'editors-void',
  templateUrl: './void.component.html',
  styleUrls: ['./void.component.scss']
})
export class VoidComponent extends EditorComponent {

  constructor() {
    super();
  }

  onClicked() {
    // Always call with a new value so Firebase emits the change.
    this.tweak.onChange(new Date());
  }
}
