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

import android.app.Application;

/**
 * The main Tweakr manager.
 *
 * Whenever you add {@link com.google.tweakr.annotations.Tweak} annotations
 * to the members of an object, make sure to call {@link #register(Object)} in the object's
 * constructor so it can find the annotations and register each annotated member. For some Android
 * objects you may want to call it during onCreate() instead of the constructor, so you can
 * initialize any views or other fields before registering them.
 */
public class Tweakr {

    private static TweakrRepo repo;

    /**
     * Returns the current repo used for this session.
     */
    public static synchronized TweakrRepo getRepo() {
        if (repo == null) {
            throw new IllegalStateException("Must call TweakrRepo.setRepo() first.");
        }
        return repo;
    }

    /**
     * Optionally sets the repo to be used for this session.
     *
     * You must set this before any other calls to methods in this class (e.g.
     * {@link #register(Object)}). Best practice is to set it in your {@link Application#onCreate()}
     *
     */
    public static synchronized void setRepo(TweakrRepo repo) {
        if (Tweakr.repo != null) {
            throw new IllegalStateException("Repo has already been set. You may only setRepo once, before any calls to register() or addListener()");
        }
        Tweakr.repo = repo;
    }

    /**
     * {@see #register(Object, String)}
     */
    public static void register(Object target) {
        register(target, null);
    }

    /**
     * Registers all members of target that are annotated with
     * {@link com.google.tweakr.annotations.Tweak}.
     *
     * Any annotated fields with children must be non-null at the time of registration.
     *
     * @param target the object containing members to register.
     * @param namePrefix An optional prefix for each Tweak. Use this if you want to differentiate
     *                   multiple objects in the UI: instead of changing a value and changing all
     *                   objects that share that member, you can add a unique namePrefix for each
     *                   object (or group of objects) so that the changes only affect a subset of
     *                   the targets.
     */
    public static void register(Object target, String namePrefix) {
        TweakrRegistry.get(getRepo()).register(target, namePrefix);
    }

    /**
     * Registers all members of target that are annotated with
     * {@link com.google.tweakr.annotations.Tweak}.
     *
     * Any annotated fields with children must be non-null at the time of registration.
     *
     * @param target the object containing members to register.
     * @param clazz the type of object to gather fields from. Use this in superclasses, since Tweakr
     *              will only gather the declared fields of the given class, not inherited fields.
     * @param namePrefix An optional prefix for each Tweak. Use this if you want to differentiate
     *                   multiple objects in the UI: instead of changing a value and changing all
     *                   objects that share that member, you can add a unique namePrefix for each
     *                   object (or group of objects) so that the changes only affect a subset of
     *                   the targets.
     */
    public <T> void register(T target, Class<? extends T> clazz,  String namePrefix) {
        TweakrRegistry.get(getRepo()).register(target, clazz, namePrefix);
    }

    /**
     * {@see TweakrRepo#addListener}
     */
    public static void addListener(TweakrRepo.OnChangeListener listener) {
        getRepo().addListener(listener);
    }
    /**
     * {@see TweakrRepo#removeListener}
     */
    public static void removeListener(TweakrRepo.OnChangeListener listener) {
        getRepo().removeListener(listener);
    }
}
