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

import { Component, Input, OnInit } from '@angular/core';
import { AngularFireDatabase } from '@angular/fire/database';
import { Observable, of } from 'rxjs';
import { flatMap, filter, tap } from 'rxjs/operators';
import { Tweak } from './Tweak';

@Component({
  selector: 'app-tweakr',
  templateUrl: './tweakr.component.html',
  styleUrls: ['./tweakr.component.scss']
})
export class TweakrComponent implements OnInit {
  @Input() tweakrRoot: string;

  tweaks: Tweak[]|undefined;

  constructor(private db: AngularFireDatabase) {}

  ngOnInit() {
    const tweakr = this.db.object(this.tweakrRoot);
    // TODO: unsubscribe from this on destroy.
    tweakr.valueChanges()
      .pipe(
        tap(root => {
          // Clear all if it was deleted.
          if (!root) {
            this.tweaks = null;
          }
        }),
        filter(root => !!root),
        flatMap((root: {}) =>
          of(Object.keys(root).map(key => new Tweak(key, root[key], (value: any) => {
            this.db.object(this.tweakrRoot + '/' + key).update({value});
          })))
        )
      )
      .subscribe((updates) => {
        //console.log('Tweak updated ', updates);
        // We have to manually update our models, so Angular doesn't clobber everything
        // and rerender it, which makes you lose hold of the range inputs.
        if (!this.tweaks) {
          this.tweaks = updates;
        } else {
          for (const update of updates) {
            let isFound = false;
            for (const tweak of this.tweaks) {
              if (tweak.id === update.id) {
                isFound = true;
                tweak.set(update);
              }
            }
            // Add any new ones.
            if (!isFound) {
              this.tweaks.push(update);
            }
            // NOTE: we don't have to worry about removing old ones right now,
            // since the Android client can't do that anyway, and the Reset All is handed higher up.
          }
        }
      });
  }
}
