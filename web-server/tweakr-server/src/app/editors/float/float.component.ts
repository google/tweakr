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

import { Component, OnInit } from '@angular/core';
import { EditorComponent } from '../editor/editor.component';

@Component({
  selector: 'editors-float',
  templateUrl: './float.component.html',
  styleUrls: ['./float.component.scss']
})
export class FloatComponent extends EditorComponent {

  constructor() {
    super();
  }

  rangeMin() {
    return this.tweak.min * this.getValueMultiplier();
  }

  rangeMax() {
    return this.tweak.max * this.getValueMultiplier();
  }

  rangeValue() {
    return this.tweak.value * this.getValueMultiplier();
  }

  rangeOnChange(value) {
    return this.tweak.onChange(+value / this.getValueMultiplier());
  }

  /**
   * This adjusts the value from the range in case we are using
   * floats/fractions, which the range input does not natively support.
   */
  getValueMultiplier() {
    if (this.tweak.max < 10 && this.tweak.min > -10) {
      return 100;
    }

    return 1;
  }
}

