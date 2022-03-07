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

package com.google.tweakr.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import android.util.ArrayMap;
import android.util.ArraySet;

import com.google.tweakr.TextUtils;
import com.google.tweakr.TweakMetadata;
import com.google.tweakr.TweakrRepo;
import com.google.tweakr.types.EnumValueType;
import com.google.tweakr.types.PrimitiveValueType;
import com.google.tweakr.types.ValueType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * A local TweakrRepo for autogenerating a Preferences screen UI in the app itself. You can use this
 * in place of the TweakrFirebaseRepo if you want local control or don't want Firebase/internet.
 */
public class TweakrPreferencesRepo implements TweakrRepo {
    private static final String TAG = "TweakrPreferencesRepo";

    private static final String METADATA_PREFS_NAME = "__TWEAKR_METADATA_PREFS_NAME__v1";

    private static final String METADATA_PREFS_FIELD_TARGET_ID = "METADATA_PREFS_FIELD_TARGET_ID";
    private static final String METADATA_PREFS_FIELD_VALUETYPE = "METADATA_PREFS_FIELD_VALUETYPE";
    private static final String METADATA_PREFS_FIELD_INITIALVALUE = "METADATA_PREFS_FIELD_INITIALVALUE";
    private static final String METADATA_PREFS_FIELD_ALL_NAMES = "METADATA_PREFS_FIELD_ALL_NAMES";
    private static final String METADATA_PREFS_FIELD_POSSIBLE_VALUES = "METADATA_PREFS_FIELD_POSSIBLE_VALUES";
    private static final String METADATA_PREFS_FIELD_DESCRIPTION = "METADATA_PREFS_FIELD_DESCRIPTION";

    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences metadataPrefs;

    private final List<OnChangeListener> listeners = new ArrayList<>();
    private final Map<String, ValueType> valueTypes = new ArrayMap<>();
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener = (prefs, key) -> {
        for (OnChangeListener listener : listeners) {
            try {
                listener.onFieldChanged(key, valueTypes.get(key).convert(convertValue(key)));
            } catch (Exception e) {
                Log.e(TAG, "Failed to convert value: perhaps ValueType is not implemented?", e);
            }
        }
    };

    /**
     * Creates a local repo that saves values to local default shared preferences.
     */
    public TweakrPreferencesRepo(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context), context.getSharedPreferences(METADATA_PREFS_NAME, Context.MODE_PRIVATE));
    }

    /**
     * Creates a local repo that saves values to the specified local shared preferences.
     *
     * @param context
     * @param valuesPreferences The SharedPreferences to store values in. This must be the same as your PreferenceFragment uses.
     * @param metadataPrefs The SharedPreferences to store internal metadata about Tweak fields. Best practice is to use a unique file name so that it doesn't pollute your other preferences (i.e. this should be different than valuesPreferences).
     */
    public TweakrPreferencesRepo(Context context, SharedPreferences valuesPreferences, SharedPreferences metadataPrefs) {
        this.context = context;
        prefs = valuesPreferences;
        this.metadataPrefs = metadataPrefs;
    }

    @Override
    public void addListener(OnChangeListener listener) {
        listeners.add(listener);

        if (listeners.size() == 1) {
            prefs.registerOnSharedPreferenceChangeListener(prefsListener);
        }
    }

    @Override
    public void removeListener(OnChangeListener listener) {
        listeners.remove(listener);

        if (listeners.size() == 0) {
            prefs.unregisterOnSharedPreferenceChangeListener(prefsListener);
        }
    }

    @Override
    public void add(String name, int targetId, ValueType valueType, Object initialValue, TweakMetadata tweakMetadata) {
        Set<String> preferencesSet =
            metadataPrefs.getStringSet(METADATA_PREFS_FIELD_ALL_NAMES, new ArraySet<>());

        Set<String> set = new ArraySet<>(preferencesSet);
        set.add(name);

        valueTypes.put(name, valueType);

        metadataPrefs.edit()
                .putInt(name + METADATA_PREFS_FIELD_TARGET_ID, targetId)
                .putString(name + METADATA_PREFS_FIELD_VALUETYPE, valueType.getName())
                .putString(name + METADATA_PREFS_FIELD_INITIALVALUE, initialValue == null ? null : initialValue.toString())
                .putStringSet(name + METADATA_PREFS_FIELD_POSSIBLE_VALUES, new ArraySet<>(valueType.getPossibleValues()))
                .putString(name + METADATA_PREFS_FIELD_DESCRIPTION, tweakMetadata.getDescription())
                .putStringSet(METADATA_PREFS_FIELD_ALL_NAMES, set)
                .apply();

        // Send out the previously saved value to listeners immediately.
        if (prefs.contains(name)) {
            prefsListener.onSharedPreferenceChanged(prefs, name);
        }
    }

    /**
     * Adds all the Tweaks to the provided PreferenceGroup. You can make a group and then add it to
     * your PreferenceFragment.getPreferenceScreen().
     *
     * @param screen the group to add all Tweakr preferences to.
     */
    public void populatePreferences(PreferenceGroup screen) {
        Set<String> set = metadataPrefs.getStringSet(METADATA_PREFS_FIELD_ALL_NAMES, new ArraySet<>());

        for (String name : set) {
            int targetId = metadataPrefs.getInt(name + METADATA_PREFS_FIELD_TARGET_ID, -1);
            String valueType = metadataPrefs.getString(name + METADATA_PREFS_FIELD_VALUETYPE, null);
            String initialValue = metadataPrefs.getString(name + METADATA_PREFS_FIELD_INITIALVALUE, null);
            String description = metadataPrefs.getString(name + METADATA_PREFS_FIELD_DESCRIPTION, null);
            Set<String> possibleValues = metadataPrefs.getStringSet(name + METADATA_PREFS_FIELD_POSSIBLE_VALUES, new ArraySet<>());

            try {
                Preference pref = createPreference(name, valueType, initialValue, possibleValues);

                pref.setKey(name);
                pref.setTitle(name);
                if (!TextUtils.isEmpty(description)) {
                    pref.setSummary(description);
                }

                if (!screen.addPreference(pref)) {
                    Log.e(TAG, "Failed to add preference for " + name);
                }
            } catch (ClassCastException e) {
                Log.e(TAG, "Failed to convert saved value for " + name, e);
            }
        }
    }

    @NonNull
    private Preference createPreference(String name, String valueType, String initialValue, Set<String> possibleValues) throws ClassCastException {

        switch (valueType) {
             case PrimitiveValueType.TYPE_FLOAT: {
                 PrimitiveValueType type = new PrimitiveValueType(valueType);
                 SeekBarPreference pref = new SeekBarPreference(context);
                 float initialFloat = (Float) type.convert(initialValue);
                 pref.setDefaultValue(initialFloat);

                 float curValue = prefs.getInt(name, (int) initialFloat);
                 pref.setMin(-500);
                 pref.setMax(500);
                 pref.setValue((int) curValue);

                 return pref;
             }
             case PrimitiveValueType.TYPE_INT: {
                 PrimitiveValueType type = new PrimitiveValueType(valueType);
                 SeekBarPreference pref = new SeekBarPreference(context);
                 Integer initialFloat = (Integer) type.convert(initialValue);
                 pref.setDefaultValue(initialFloat);

                 int curValue = prefs.getInt(name, initialFloat);
                 pref.setMin(-500);
                 pref.setMax(500);
                 pref.setValue((int) curValue);

                 return pref;
             }
             case PrimitiveValueType.TYPE_BOOLEAN: {
                 PrimitiveValueType type = new PrimitiveValueType(valueType);
                 SwitchPreference pref = new SwitchPreference(context);
                 Boolean initialFloat = (Boolean) type.convert(initialValue);
                 pref.setDefaultValue(initialFloat);

                 boolean curValue = prefs.getBoolean(name, initialFloat);
                 pref.setChecked(curValue);

                 return pref;
             }
             case EnumValueType.TYPE_ENUM: {
//                 DropDownPreference pref = new DropDownPreference(context);
                 ListPreference pref = new ListPreference(context);
                 pref.setDefaultValue(initialValue);

                 String[] entries = possibleValues.toArray(new String[0]);
                 pref.setEntries(entries);
                 pref.setEntryValues(entries);

                 String curValue = prefs.getString(name, initialValue);
                 pref.setValue(curValue);

                 return pref;
             }
            default:
                EditTextPreference pref = new EditTextPreference(context);
                pref.setDefaultValue(initialValue);

                pref.setText(prefs.getString(name, initialValue));
                return pref;
        }
    }

    private Object convertValue(String name) {
        String valueType = metadataPrefs.getString(name + METADATA_PREFS_FIELD_VALUETYPE, null);
        switch (valueType) {
            case PrimitiveValueType.TYPE_FLOAT:
                // Fall-through since SeekPref only stores Ints.
            case PrimitiveValueType.TYPE_INT: {
                return prefs.getInt(name, 0);
            }
            case PrimitiveValueType.TYPE_BOOLEAN: {
                return prefs.getBoolean(name, false);
            }
            case PrimitiveValueType.TYPE_STRING:
                // fall-through!
            default:
                return prefs.getString(name, null);
        }
    }
}
