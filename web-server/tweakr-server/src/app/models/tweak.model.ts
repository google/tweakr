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

export class TweakMetadata {
  description?: string;
}

/**
 * Main data model for a Tweakable parameter.
 */
export class Tweak {
  initialValue: number;
  possibleValues: string[] | undefined;
  value: number | undefined;
  type: string;
  metadata?: TweakMetadata;
  min: number;
  max: number;

  constructor(public readonly id: string, data: any, private onChangeListener: (value: any) => void) {
    console.log('new Tweak ' + id, data);

    this.set(data);
  }

  /**
   * Sets all fields on this object EXCEPT for value, so we don't clobber inputs.
   *
   * @param data
   */
  set(data: any) {
    this.initialValue = data.initialValue;
    this.possibleValues = data.possibleValues;
    this.type = data.type;

    this.metadata = {
      ...this.metadata,
      ...data.metadata
    };

    if (this.value === undefined) {
      if (data.value === undefined) {
        this.value = data.initialValue;
      } else {
        this.value = data.value;
      }
    }

    if (this.min === undefined) {
      this.calculateMin(this.initialValue);
    }
    if (this.max === undefined) {
      this.calculateMax(this.initialValue);
    }
  }

  private calculateMin(currentValue: number) {
    if (this.isProbablyNormalized(currentValue)) {
      this.min = 0;
    } else {
      this.min = Math.min(0, currentValue - Math.abs(currentValue / 2));
    }
  }

  private calculateMax(currentValue: number) {
    if (this.isProbablyNormalized(currentValue)) {
      this.max = 1;
    } else {
      this.max = currentValue + Math.abs(currentValue / 2);
    }
  }

  /**
   * Call to set a new value.
   *
   * @param newValue the new value
   */
  onChange(newValue: any): void {
    if (this.type === 'float' || this.type === 'int') {
      this.value = +newValue;

      if (this.value > this.max || this.isProbablyNormalized(this.value)) {
        this.calculateMax(this.value);
      }

      if (this.value < this.min || this.isProbablyNormalized(this.value)) {
        this.calculateMin(this.value);
      }
    } else {
      this.value = newValue;
    }

    this.onChangeListener(this.value);
  }

  resetToInitialValue() {
    this.onChange(this.initialValue);
  }

  private isProbablyNormalized(currentValue) {
    return this.type === 'float' && currentValue <= 1 && currentValue >= 0
      && this.initialValue <= 1 && this.initialValue >= 0;
  }
}
