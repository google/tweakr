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

import {Component, OnInit} from '@angular/core';
import {EditorComponent} from '../editor/editor.component';

function parseColorComponent(component: string): number {
  return Math.min(255, parseInt(component, 10));
}

function getColorComponent(androidColor: number, index: number): number {
  /* tslint:disable no-bitwise */
  return androidColor >> (index * 8) & 0xff;
  /* tslint:enable */
}

function androidToRgba(androidColor: number): string {
  return 'rgba(' + getColorComponent(androidColor, 2) + ',' +
      getColorComponent(androidColor, 1) + ',' +
      getColorComponent(androidColor, 0) + ',' +
      (getColorComponent(androidColor, 3) / 255) + ')';
}

function rgbaToAndroid(rgbaColor: string): number {
  const rgba = rgbaColor.substring(5, rgbaColor.length - 1).split(',');

  /* tslint:disable no-bitwise */
  const hex = Math.round(Number(rgba[3]) * 255) << 24 |
      parseColorComponent(rgba[0]) << 16 | parseColorComponent(rgba[1]) << 8 |
      parseColorComponent(rgba[2]) << 0;
  /* tslint:enable */
  return hex;
}

@Component({
  selector: 'editors-color',
  templateUrl: './color.component.html',
  styleUrls: ['./color.component.scss']
})
export class ColorComponent extends EditorComponent implements OnInit {
  color: string|undefined;

  constructor() {
    super();
  }

  ngOnInit() {
    this.color = androidToRgba(this.tweak.value);
  }

  onColorChange(color) {
    this.color = color;
    this.tweak.onChange(rgbaToAndroid(this.color));
  }
}
