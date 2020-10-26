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
import {ActivatedRoute} from '@angular/router';

import {AngularFireAuth} from '@angular/fire/auth';
import {AngularFireDatabase} from '@angular/fire/database';
import {auth} from 'firebase/app';

import {MatDialog, MatDialogRef} from '@angular/material/dialog';

import { ConfirmationDialogComponent } from '../dialogs/confirmation-dialog.component';
import { UserPromptDialogComponent } from '../dialogs/user-prompt-dialog.component';

import { Observable, of, Subscription } from 'rxjs';
import { first, flatMap, filter, toArray } from 'rxjs/operators';

// You can use a default user here, or write something more complicated like
// prompting the user for login at the launch of the app. If you use email
// authentication, you will need to enable it in the Android app as well.
const DEFAULT_EMAIL = 'YOUR_EMAIL@DOMAIN.com';
const DEFAULT_PASSWORD = 'YOUR PASSWORD HERE';

const TWEAKR_ROOT = 'tweakr_v2';

@Component({
  selector: 'app-tweak',
  templateUrl: './tweak.component.html',
  styleUrls: ['./tweak.component.scss']
})
export class TweakComponent implements OnInit {
  tweakrRoot = TWEAKR_ROOT;
  selectedUserKey: string|undefined;
  userKeys: string[] = [];
  isLoggedIn = false;
  statusText: string|undefined;

  private subscription?: Subscription;

  private hasPrompted = false;

  private userKeyDialogRef?: MatDialogRef<UserPromptDialogComponent>;

  constructor(public dialog: MatDialog, private route: ActivatedRoute,
      private afAuth: AngularFireAuth, private db: AngularFireDatabase) {}

  ngOnInit() {
    this.checkLoginStatus();
  }

  checkLoginStatus() {
    this.statusText = 'Logging in...';

    this.subscription?.unsubscribe();
    this.subscription = this.db.object(this.tweakrRoot)
        .valueChanges()
        .subscribe(
            (root) => {
              console.log('root: ', root);
              this.userKeys = root ? Object.keys(root) : [];
              if (this.userKeys.length > 0 && this.selectedUserKey == undefined) {
                this.selectedUserKey = this.userKeys[0];
              }

              this.isLoggedIn = true;
              this.statusText = undefined;

              this.showPromptIfNeeded();
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

      if (this.isLoggedIn) {
        this.checkLoginStatus();
      }
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
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Reset all Tweaks',
        message: 'This will delete all Tweaks. You will need to close your app and open it again to restore them with the default values.',
        action: 'Delete all'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      if (!result) return;

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
    });
  }

  showPromptIfNeeded() {
    // If dialog is already open, refresh the list of valid codes.
    if (this.userKeyDialogRef && this.userKeyDialogRef.componentInstance?.data) {
      this.userKeyDialogRef.componentInstance.data.validCodes = this.userKeys;
    }
    if (this.hasPrompted) return;
    const params = this.route.snapshot.queryParamMap;
    if (params.get('promptForUserKey') == 'true') {
      const message = params.get('promptMessage');

      this.promptForUserKey(message);

      this.hasPrompted = true;
    }
  }

  promptForUserKey(message: string|undefined) {
    this.userKeyDialogRef = this.dialog.open(UserPromptDialogComponent, {
      disableClose: true,
      data: {
        message,
        validCodes: this.userKeys
      }
    });

    this.userKeyDialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);

      if (result && this.userKeys.includes(result)) {
        this.selectedUserKey = result;
      }

      this.userKeyDialogRef = null;
    });
  }
}
