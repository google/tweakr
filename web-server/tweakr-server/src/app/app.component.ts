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
import {ActivatedRoute, Router, NavigationStart} from '@angular/router';

import {filter} from 'rxjs/operators';

import {getFirebaseConfig} from './firebase_config';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationStart))
      .subscribe(ev => this.onNavigationStart(ev));
  }

  onNavigationStart(event) {
    if (event.url == '/') {
      if (!getFirebaseConfig(this.route)) {
        this.router.navigate(['setup']);
      } else {
        this.router.navigate(['tweak']);
      }
    }
  }
}
