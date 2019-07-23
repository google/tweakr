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

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.tweakr.TweakrFirebaseRepo;

import java.util.concurrent.CompletableFuture;

public class TweakrFirebaseRepoEmailAuth extends TweakrFirebaseRepo {
    private static final String TAG = "TweakrFirebaseRepoEmail";

    // You can use a default user here, or write something more complicated like prompting the user
    // for login at the launch of the app.
    // Note these should match the ones in in the web server.
    private static final String DEFAULT_EMAIL = "YOUR_EMAIL@DOMAIN.com";
    private static final String DEFAULT_PASSWORD = "YOUR PASSWORD HERE";

    private final FirebaseAuth auth;

    public TweakrFirebaseRepoEmailAuth() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    @NonNull
    protected CompletableFuture<FirebaseUser> authenticate() {
        return authenticate(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    @NonNull
    private CompletableFuture<FirebaseUser> authenticate(String email, String password) {
        CompletableFuture<FirebaseUser> future = new CompletableFuture<>();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            future.complete(auth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            future.completeExceptionally(task.getException());
                        }
                    });

        } else {
            future.complete(currentUser);
        }

        return future;
    }
}
