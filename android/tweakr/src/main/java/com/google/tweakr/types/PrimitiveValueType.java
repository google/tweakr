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

import java.util.Objects;

public class PrimitiveValueType implements ValueType {
    public static final String TYPE_STRING = "string";
    public static final String TYPE_INT = "int";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_UNKNOWN = "unknown";

    private final String name;

    public PrimitiveValueType(String name) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getDefault() {
        if (TYPE_STRING.equals(name)) {
            return "";
        } else if (TYPE_INT.equals(name) || TYPE_FLOAT.equals(name)) {
            return 0;
        } else if (TYPE_BOOLEAN.equals(name)) {
            return false;
        }

        return null;
    }

    @Override
    public Object convert(Object newValue) {
        if (TYPE_STRING.equals(name)) {
            return String.valueOf(newValue);
        } else if (TYPE_INT.equals(name)) {
            return safelyConvertLongToInt((long) newValue, (int) getDefault());
        } else if (TYPE_FLOAT.equals(name)) {
            if (newValue instanceof Float || newValue instanceof Double) {
                return ((Number) newValue).floatValue();
            } else if (newValue instanceof String) {
                return Float.valueOf((String)newValue);
            }
        } else if (TYPE_BOOLEAN.equals(name)) {
            if (newValue instanceof String) {
                return Boolean.valueOf((String)newValue);
            }
        }

        return newValue;
    }

    public static int safelyConvertLongToInt(Object newValue, int fallbackDefault) {
        try {
            return Math.toIntExact((long) newValue);
        } catch (ArithmeticException e) {
            long result = (long)newValue;
            if (result >= Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            } else if (result <= Integer.MIN_VALUE){
                return Integer.MIN_VALUE;
            } else {
                return fallbackDefault;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveValueType that = (PrimitiveValueType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
