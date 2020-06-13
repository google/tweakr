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

package com.google.tweakr.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnumValueType<T> implements ValueType {
    public static final String TYPE_ENUM = "enum";

    private final T[] enumConstants;

    public EnumValueType(Class<T> clazz) {
        this.enumConstants = clazz.getEnumConstants();
    }

    @Override
    public String getName() {
        return TYPE_ENUM;
    }

    @Override
    public Object getDefault() {
        return enumConstants[0];
    }

    @Override
    public Object convert(Object newValue) {
        for (T constant : this.enumConstants) {
            if (constant.toString().equals(newValue.toString())) {
                return constant;
            }
        }

        return getDefault();
    }

    @Override
    public List<String> getPossibleValues() {
        List<String> result = new ArrayList<>(this.enumConstants.length);
        for (T constant : this.enumConstants) {
            result.add(constant.toString());
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumValueType<?> that = (EnumValueType<?>) o;
        return Arrays.equals(enumConstants, that.enumConstants);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(enumConstants);
    }
}
