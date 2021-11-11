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

package com.google.tweakr.sample;

import android.app.Application;

import com.google.tweakr.Tweakr;
import com.google.tweakr.preferences.TweakrPreferencesRepo;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable local Android preference screen for Tweakr.
        Tweakr.setRepo(new TweakrPreferencesRepo(this));

        // FIREBASE SUPPORT: Uncomment one of the below, and comment out the above line.

        // Uncomment this to enable the default no-auth Firebase Repo.
        //Tweakr.setRepo(new com.google.tweakr.TweakrFirebaseRepo());

        // Uncomment this to enable email authentication (default is anonymous/no authentication).
        // Tweakr.setRepo(new TweakrFirebaseRepoEmailAuth());
    }
}
