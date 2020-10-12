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

package com.google.tweakr.collections;

import java.lang.ref.WeakReference;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class MemberMap<K, V extends Member> {
    private final static String TAG = "MemberMap";

    protected class MemberHolder {
        public final WeakReference<Object> target;
        // TODO: don't duplicate member, just store it once in a hash.
        public final V member;

        public MemberHolder(Object target, V member) {
            this.target = new WeakReference<>(target);
            this.member = member;
        }
    }

    protected class MemberList extends ArrayList<MemberHolder> {

    }

    private final Map<K, MemberList> map = new HashMap<>();

    public boolean has(K key) {
        return map.containsKey(key);
    }

    public void add(K key, Object target, V member) {
        MemberList memberHolder = map.get(key);
        if (memberHolder == null) {
            memberHolder = new MemberList();
            map.put(key, memberHolder);
        }

        // TODO: check if target is already in the list, avoid duplicating.
        memberHolder.add(new MemberHolder(target, member));
    }

    public void set(K key, Object value) throws ReflectiveOperationException {
        MemberList memberHolder = map.get(key);
        if (memberHolder != null) {
            for (Iterator<MemberHolder> iterator = memberHolder.iterator(); iterator.hasNext(); ) {
                MemberHolder holder = iterator.next();
                Object target = holder.target.get();
                if (target == null) {
                    iterator.remove();
                } else {
                    setValue(holder, value);
                }
            }
            // TODO: should we remove the map entry if there are no more in the array?
        }
    }

    /**
     * Called with a value to pass to the member of target. Holder is guaranteed to have a non-null target.
     *
     * @param holder
     * @param value
     * @throws ReflectiveOperationException
     */
    protected abstract void setValue(MemberHolder holder, Object value) throws ReflectiveOperationException;
}
