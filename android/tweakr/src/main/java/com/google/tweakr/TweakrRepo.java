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

import com.google.tweakr.types.ValueType;

/**
 * A repo to store Tweaks and manipulate them with a UI, then notify the registry via
 * {@link OnChangeListener}.
 */
public interface TweakrRepo {
    String FIELD_SEPARATOR = ":";

    interface OnChangeListener {
        /**
         * Called when the value changes in the UI.
         *
         * @param name fully-qualified name of the member.
         * @param value the new value to be set.
         */
        void onFieldChanged(String name, Object value);
    }

    /**
     * Registers a listener to be notified whenever a value changes in the UI.
     */
    void addListener(OnChangeListener listener);

    /**
     * Removes a listener added with {@link #addListener(OnChangeListener)}
     */
    void removeListener(OnChangeListener listener);

    /**
     * Add a new Tweak.
     *  @param name fully-qualified name of the tweak: may be a path of parent objects to the actual
     *             field's name, separated by {@link #FIELD_SEPARATOR}.
     * @param targetId monotonically increasing ID of the target object.
     * @param valueType ValueType for converting the UI value before passing it to
 *                  {@link OnChangeListener#onFieldChanged}.
     * @param initialValue Initial value of the field as it exists in the target object.
     * @param tweakMetadata Metadata from the annotation for the UI to display.
     */
    void add(String name, int targetId, ValueType valueType, Object initialValue, TweakMetadata tweakMetadata);
}
