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

package com.google.tweakr;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.tweakr.types.ValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * A TweakrRepo that uses Firebase to store and manipulate values with a Web UI.
 */
public class TweakrFirebaseRepo implements TweakrRepo {
    protected static final String TAG = "TweakrRepo";

    /**
     * The default key of the root-level collection.
     */
    private static final String TABLE_TWEAKR = "tweakr_v2";

    // TODO: unregister these at some point
    private final Map<String, ValueEventListener> snapshotListeners = new HashMap<>();

    private final List<OnChangeListener> listeners = new ArrayList<>();

    public TweakrFirebaseRepo() {

    }

    /**
     * Authenticates with Firebase before adding any Tweaks.
     *
     * This will be called repeatedly so you should cache by checking FirebaseAuth.getCurrentUser(),
     * and simply returning that if it already exists.
     *
     * If your database allows anonymous users and does not require authentication, you may simply
     * return a Future that resolves to null. This is the default behavior.
     *
     * @return A Future which completes once the user has logged in. Once it completes, any Tweaks
     *         queued up will be added immediately.
     */
    @NonNull
    protected CompletableFuture<Boolean> authenticate() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Returns the Firebase database to use. You may override this if you would like to use multiple
     * Firebase instances in your app.
     */
    @NonNull
    protected FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance();
    }

    /**
     * Returns the name of the root-level collection in the database to add all Tweaks.
     *
     * Override this if you are using the same database for multiple apps.
     */
    @NonNull
    protected String getRootCollectionKey() {
        return TABLE_TWEAKR;
    }

    /**
     * Returns the user's sub-collection key. Use this if you want each user to be able to Tweak
     * their own set of values, which will not change the other users' values.
     *
     * @return an alphanumeric identifier that is unique per user.
     */
    public CompletableFuture<String> getUserKey() {
        return CompletableFuture.completedFuture("default");
    }

    @Override
    public void addListener(OnChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(OnChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void add(String name, int targetId, ValueType valueType, Object initialValue, TweakMetadata tweakMetadata) {
        authenticate()
            .thenCombine(getUserKey(), (isAuthed, key) -> key)
            .thenAccept(subCollectionKey -> {
                DatabaseReference doc = getDatabase()
                        .getReference(getRootCollectionKey() + "/" + subCollectionKey)
                        .child(name);

                doc.child("type").setValue(valueType.getName());
                doc.child("initialValue").setValue(initialValue);
                doc.child("metadata").setValue(tweakMetadata);
                List<String> possibleValues = valueType.getPossibleValues();
                if (possibleValues != null) {
                    doc.child("possibleValues").setValue(possibleValues);
                }

                DatabaseReference valueRef = doc.child("value");
                addValueListener(name, valueRef, valueType);
            })
            .exceptionally((e) -> {
                Log.e(TAG, "Failed to add target ", e);
                return null;
            });
    }

    private void addValueListener(String name, DatabaseReference valueRef, ValueType valueType) {
        // Always remove the old listener so we get a fresh value in case the object was
        // recreated/reregistered but the listener survived it.
        ValueEventListener oldListener = snapshotListeners.remove(name);
        if (oldListener != null) {
            valueRef.removeEventListener(oldListener);
        }

        ValueEventListener valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object newValue = snapshot.getValue();
                Log.d(TAG, "Current data: " + newValue);
                // TODO: allow nulls? will need to convert with ValueType
                if (newValue != null) {
                    for (OnChangeListener listener : listeners) {
                        listener.onFieldChanged(name, valueType.convert(newValue));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Value listener cancelled.", databaseError.toException());
            }
        };
        valueRef.addValueEventListener(valueListener);

        snapshotListeners.put(name, valueListener);
    }
}
