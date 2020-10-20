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
import {AngularFireAuth} from '@angular/fire/auth';
import {AngularFireDatabase} from '@angular/fire/database';
import {auth} from 'firebase/app';

import { Observable, of } from 'rxjs';
import { first, flatMap, filter, toArray } from 'rxjs/operators';

// You can use a default user here, or write something more complicated like
// prompting the user for login at the launch of the app. If you use email
// authentication, you will need to enable it in the Android app as well.
const DEFAULT_EMAIL = 'YOUR_EMAIL@DOMAIN.com';
const DEFAULT_PASSWORD = 'YOUR PASSWORD HERE';

const TWEAKR_ROOT = 'tweakr';

@Component({
  selector: 'app-tweak',
  templateUrl: './tweak.component.html',
  styleUrls: ['./tweak.component.scss']
})
export class TweakComponent implements OnInit {
  tweakrRoot = TWEAKR_ROOT;
  isLoggedIn = false;
  statusText: string|undefined;

  constructor(
      private afAuth: AngularFireAuth, private db: AngularFireDatabase) {}

  ngOnInit() {
    // TODO: add tabs to load different tweakr roots
    this.checkLoginStatus();
  }

  checkLoginStatus() {
    this.statusText = 'Logging in...';
    this.db.object(this.tweakrRoot)
        .valueChanges()
        .pipe(first())
        .subscribe(
            () => {
              this.isLoggedIn = true;
              this.statusText = undefined;
            },
            (error: {}) => {
              console.log('Error', error);
              this.isLoggedIn = false;
              this.login();
            });
  }

  async login() {
    try {
      const user = await this.afAuth.signInWithEmailAndPassword(
          DEFAULT_EMAIL, DEFAULT_PASSWORD);
      this.isLoggedIn = (user != null);
      this.statusText = undefined;
    } catch (e) {
      this.isLoggedIn = false;
      this.statusText = `Error logging in: ${e}`;
      console.error('Error logging in', e);
    }
  }

  logout() {
    this.afAuth.signOut();
  }

  async onResetAllClicked() {
    // TODO: confirmation dialog
    const tweakr = this.db.object(this.tweakrRoot);
    tweakr.valueChanges()
      .pipe(
        filter(root => !!root),
        first(),
        flatMap((root: {}) =>
          of(Object.keys(root).map(key =>
            this.db.object(this.tweakrRoot + '/' + key).remove()
          ))
        ),
        toArray()
      )
      .subscribe((items) => console.log('Deleted ', items));
  }
}
